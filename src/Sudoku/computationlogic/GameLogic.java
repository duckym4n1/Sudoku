package computationlogic;

import constants.GameState;
import constants.Rows;
import problemdomain.SudokuGame;

import java.util.*;

import static problemdomain.SudokuGame.GRID_BOUNDARY;
public class GameLogic {
    public static SudokuGame getNewGame() {
        return new SudokuGame(
                GameState.NEW,
                GameGenerator.getNewGameGrid()
        );
    }

    /**
     * Kiểm tra xem trạng thái đến (giá trị của mỗi ô vuông xảy ra là gì) của trò chơi có phải là Hoạt động hay không
     * (tức là chưa được giải quyết) hoặc Hoàn thành (tức là đã giải quyết)
     * Quy tắc:
     * - Một số không được lặp lại giữa các Hàng, ví dụ:
     * - [0, 0] == [0-8, 1] không được phép
     * - [0, 0] == [3, 4] được phép
     * - Một số không được lặp lại giữa các Cột, ví dụ:
     * - [0-8, 1] == [0, 0] không được phép
     * - [0, 0] == [3, 4] được phép
     * - Một số không được lặp lại trong GRID_BOUNDARYxGRID_BOUNDARY khu vực tương ứng trong Câu đố Sudoku
     * - [0, 0] == [1, 2] không được phép
     * - [0, 0] == [3, 4] được phép
     */
    public static GameState checkForCompletion(int[][] grid) {
        if (sudokuIsInvalid(grid)) return GameState.ACTIVE;
        if (tilesAreNotFilled(grid)) return GameState.ACTIVE;
        return GameState.COMPLETE;
    }

    /**
     * Đi qua tất cả các ô và xác định xem có bất kỳ ô nào không phải là 0.
     * Lưu ý: GRID_BOUNDARY = GRID_BOUNDARY
     */
    public static boolean tilesAreNotFilled(int[][] grid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                if (grid[xIndex][yIndex] == 0) return true;
            }
        }
        return false;
    }

    /**
     * Kiểm tra xem trạng thái hoàn thành hay chưa hoàn thành hiện tại của trò chơi vẫn là trạng thái hợp lệ của trò chơi Sudoku,
     * liên quan đến cột, hàng và hình vuông
     */
    public static boolean sudokuIsInvalid(int[][] grid) {
        if (rowsAreInvalid(grid)) return true;
        if (columnsAreInvalid(grid)) return true;
        if (squaresAreInvalid(grid)) return true;
        else return false;
    }

    public static boolean squaresAreInvalid(int[][] grid) {
        //Ba ô vuông trên cùng
        if (rowOfSquaresIsInvalid(Rows.TOP, grid)) return true;

        //Ba ô vuông ở giữa
        if (rowOfSquaresIsInvalid(Rows.MIDDLE, grid)) return true;

        //Ba ô vuông dưới cùng
        if (rowOfSquaresIsInvalid(Rows.BOTTOM, grid)) return true;

        return false;
    }

    private static boolean rowOfSquaresIsInvalid(Rows value, int[][] grid) {
        switch (value) {
            case TOP:
                //x ĐẦU TIÊN = 0
                if (squareIsInvalid(0, 0, grid)) return true;
                //x Thứ 2 = 3
                if (squareIsInvalid(0, 3, grid)) return true;
                //x thứ 3 = 6
                if (squareIsInvalid(0, 6, grid)) return true;

                //Nếu không, hình vuông có vẻ hợp lệ
                return false;

            case MIDDLE:
                if (squareIsInvalid(3, 0, grid)) return true;
                if (squareIsInvalid(3, 3, grid)) return true;
                if (squareIsInvalid(3, 6, grid)) return true;
                return false;

            case BOTTOM:
                if (squareIsInvalid(6, 0, grid)) return true;
                if (squareIsInvalid(6, 3, grid)) return true;
                if (squareIsInvalid(6, 6, grid)) return true;
                return false;

            default:
                return false;
        }
    }

    public static boolean squareIsInvalid(int yIndex, int xIndex, int[][] grid) {
        int yIndexEnd = yIndex + 3;
        int xIndexEnd = xIndex + 3;

        List<Integer> square = new ArrayList<>();

        while (yIndex < yIndexEnd) {

            while (xIndex < xIndexEnd) {
                square.add(
                        grid[xIndex][yIndex]
                );
                xIndex++;
            }

            //Đặt lại X về giá trị ban đầu bằng cách trừ 2
            xIndex -= 3;

            yIndex++;
        }

        //Nếu hình vuông có lặp lại, trả về true
        if (collectionHasRepeats(square)) return true;
        return false;
    }

    public static boolean columnsAreInvalid(int[][] grid) {
        for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
            List<Integer> row = new ArrayList<>();
            for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
                row.add(grid[xIndex][yIndex]);
            }

            if (collectionHasRepeats(row)) return true;
        }

        return false;
    }

    public static boolean rowsAreInvalid(int[][] grid) {
        for (int yIndex = 0; yIndex < GRID_BOUNDARY; yIndex++) {
            List<Integer> row = new ArrayList<>();
            for (int xIndex = 0; xIndex < GRID_BOUNDARY; xIndex++) {
                row.add(grid[xIndex][yIndex]);
            }

            if (collectionHasRepeats(row)) return true;
        }

        return false;
    }

    public static boolean collectionHasRepeats(List<Integer> collection) {
        //Đếm số lần xuất hiện của các số nguyên từ 1-GRID_BOUNDARY. Nếu Collections.frequency trả về giá trị lớn hơn 1,
        //thì hình vuông không hợp lệ (tức là một số khác không đã được lặp lại trong một hình vuông)
        for (int index = 1; index <= GRID_BOUNDARY; index++) {
            if (Collections.frequency(collection, index) > 1) return true;
        }

        return false;
    }
}

