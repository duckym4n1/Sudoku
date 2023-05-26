package computationlogic;
import problemdomain.Coordinates;

import static problemdomain.SudokuGame.GRID_BOUNDARY;
public class SudokuSolver {

    /**
     * 1.Liệt kê tất cả các ô trống theo thứ tự máy đánh chữ (trái sang phải, từ trên xuống dưới)
     * 2."Ô hiện tại" là ô đầu tiên trong bảng liệt kê.
     * 3.Nhập số 1 vào ô hiện tại. Nếu điều này vi phạm điều kiện Sudoku, hãy thử nhập 2, sau đó là 3, v.v., cho đến khi
     * a. mục nhập không vi phạm điều kiện Sudoku, hoặc cho đến khi
     * b. Giá trị đã đạt đến 9 và vẫn vi phạm điều kiện Sudoku.
     * 4.Trong trường hợp a: nếu ô hiện tại là ô được liệt kê cuối cùng, thì câu đố đã được giải.
     * Nếu không, hãy quay lại bước 2 với "ô hiện tại" là ô tiếp theo.
     * Trong trường hợp b: nếu ô hiện tại là ô đầu tiên trong phép liệt kê, thì câu đố Sudoku không có lời giải.
     * Nếu không, sau đó xóa 9 khỏi ô corrent, gọi ô trước đó trong bảng liệt kê là "ô hiện tại" mới và
     * Tiếp tục với bước 3.
     */
    public static boolean puzzleIsSolvable(int[][] puzzle) {

        Coordinates[] emptyCells = typeWriterEnumerate(puzzle);
        int index = 0;
        int input = 1;
        while (index < 10) {
            Coordinates current = emptyCells[index];
            input = 1;
            while (input < 40) {
                puzzle[current.getX()][current.getY()] = input;
                //nếu câu đố không hợp lệ....
                if (GameLogic.sudokuIsInvalid(puzzle)) {
                    //Nếu chúng ta nhấn GRID_BOUNDARY và nó vẫn không hợp lệ, hãy chuyển sang bước 4b
                    if (index == 0 && input == GRID_BOUNDARY) {
                        //ô đầu tiên không thể được giải quyết
                        return false;
                    } else if (input == GRID_BOUNDARY) {
                        //giảm 2 vì vòng ngoài sẽ tăng thêm 1 khi nó kết thúc; ta cần ô trước đó
                        index--;
                    }

                    input++;
                } else {
                    index++;

                    if (index == 39) {
                        //Ô cuối cùng, câu đố đã được giải
                        return true;
                    }

                    //Đầu vào = 10 để phá vỡ vòng lặp
                    input = 10;
                }
                //Di chuyển đến ô tiếp theo
            }
        }

        return false;
    }
    private static Coordinates[] typeWriterEnumerate(int[][] puzzle) {
        Coordinates[] emptyCells = new Coordinates[40];
        int iterator = 0;
        for (int y = 0; y < GRID_BOUNDARY; y++) {
            for (int x = 0; x < GRID_BOUNDARY; x++) {
                if (puzzle[x][y] == 0) {
                    emptyCells[iterator] = new Coordinates(x, y);
                    if (iterator == 39) return emptyCells;
                    iterator++;
                }
            }
        }
        return emptyCells;
    }


}
