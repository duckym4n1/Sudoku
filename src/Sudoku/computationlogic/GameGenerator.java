package computationlogic;
import problemdomain.Coordinates;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import static problemdomain.SudokuGame.GRID_BOUNDARY;

public class GameGenerator {
    public static int[][] getNewGameGrid() {
    return unsolveGame(getSolvedGame());
}

    /**
     * 1. Sinh map đấu 9x9 trên 1 mảng 2 chiều
     * 2. Đối với mỗi giá trị trong phạm vi 1..9, hãy phân bổ giá trị đó 9 lần dựa trên các ràng buộc sau:
     * - Một tọa độ ngẫu nhiên trên lưới được chọn. Nếu nó trống, một giá trị ngẫu nhiên được phân bổ.
     * - Việc phân bổ kết quả không được tạo ra các hàng, cột hoặc hình vuông không hợp lệ.
     * - Nếu việc phân bổ tạo ra một trò chơi không hợp lệ
     */
    private static int[][] getSolvedGame() {
        Random random = new Random(System.currentTimeMillis());
        int[][] newGrid = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        //Giá trị đại diện cho các giá trị tiềm năng cho mỗi ô vuông. Mỗi giá trị phải được phân bổ 9 lần.
        for (int value = 1; value <= GRID_BOUNDARY; value++) {
            //Phân bổ đề cập đến số lần mà một hình vuông đã được đưa ra một giá trị.
            int allocations = 0;

            //Nếu quá nhiều lần phân bổ được thực hiện kết thúc bằng một trò chơi không hợp lệ, chúng tôi sẽ lấy lần
            // phân bổ gần đây nhất được lưu trữ trong Danh sách bên dưới và đặt lại tất cả chúng về 0 (trống).
            int interrupt = 0;

            //Theo dõi những gì đã được phân bổ trong khung hiện tại của vòng lặp
            List<Coordinates> allocTracker = new ArrayList<>();

            int attempts = 0;

            while (allocations < GRID_BOUNDARY) {

                if (interrupt > 200) {
                    allocTracker.forEach(coord -> {
                        newGrid[coord.getX()][coord.getY()] = 0;
                    });

                    interrupt = 0;
                    allocations = 0;
                    allocTracker.clear();
                    attempts++;

                    if (attempts > 500) {
                        clearArray(newGrid);
                        attempts = 0;
                        value = 1;
                    }
                }

                int xCoordinate = random.nextInt(GRID_BOUNDARY);
                int yCoordinate = random.nextInt(GRID_BOUNDARY);

                if (newGrid[xCoordinate][yCoordinate] == 0) {
                    newGrid[xCoordinate][yCoordinate] = value;

                    //Nếu giá trị dẫn đến một trò chơi không hợp lệ, hãy gán lại phần tử đó thành 0 và thử lại
                    if (GameLogic.sudokuIsInvalid(newGrid)) {
                        newGrid[xCoordinate][yCoordinate] = 0;
                        interrupt++;
                    }
                    //Nếu không, hãy chỉ ra rằng một giá trị đã được phân bổ và thêm nó vào trình theo dõi phân bổ.
                    else {
                        allocTracker.add(new Coordinates(xCoordinate, yCoordinate));
                        allocations++;
                    }
                }
            }
        }
        return newGrid;
    }

    /**
     * Mục đích của chức năng này là lấy một trò chơi đã được giải quyết (và do đó được chứng minh là có thể giải quyết được),
     * và gán ngẫu nhiên một số ô nhất định bằng 0. Dường như không có thẳng
     * Cách chuyển tiếp để kiểm tra xem một câu đố vẫn có thể giải được sau khi loại bỏ các ô, ngoài việc sử dụng một thuật toán khác
     * để cố gắng giải quyết lại vấn đề.
     *
     * 1. Sao chép các giá trị của SolvedGame sang một Array mới (tạo thành một helper)
     * 2. Loại bỏ ngẫu nhiên 40 Giá trị khỏi Mảng mới.
     * 3. Kiểm tra Array mới cho solvablility.
     * 4a. Có thể giải quyết -> trả về Mảng mới
     * 4b. Quay lại Bước 1
     */
    private static int[][] unsolveGame(int[][] solvedGame) {
        Random random = new Random(System.currentTimeMillis());

        boolean solvable = false;

        int[][] solvableArray = new int[GRID_BOUNDARY][GRID_BOUNDARY];

        while (solvable == false){

            //Lấy các giá trị từ trò chơi đã giải quyết và ghi vào trò chơi mới chưa được giải quyết; tức là đặt lại về trạng thái ban đầu
            SudokuUtilities.copySudokuArrayValues(solvedGame, solvableArray);

            //Xóa 40 số ngẫu nhiên
            int index = 0;
            while (index < 40) {
                int xCoordinate = random.nextInt(GRID_BOUNDARY);
                int yCoordinate = random.nextInt(GRID_BOUNDARY);

                if (solvableArray[xCoordinate][yCoordinate] != 0) {
                    solvableArray[xCoordinate][yCoordinate] = 0;
                    index++;
                }
            }

            int[][] toBeSolved = new int[GRID_BOUNDARY][GRID_BOUNDARY];
            SudokuUtilities.copySudokuArrayValues(solvableArray, toBeSolved);
            //Kiểm tra xem kết quả có thể giải quyết được không
            solvable = SudokuSolver.puzzleIsSolvable(toBeSolved);
            System.out.println(solvable);
        }

        return solvableArray;
    }

    private static void clearArray(int[][] newGrid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                newGrid[xIndex][yIndex] = 0;
            }
        }
    }

}