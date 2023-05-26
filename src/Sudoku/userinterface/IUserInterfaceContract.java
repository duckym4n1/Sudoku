package userinterface;

import problemdomain.SudokuGame;

public interface IUserInterfaceContract {

    interface EventListener {
        void onSudokuInput(int x, int y, int input);
        void onDialogClick();
    }



    interface View {
        void setListener(IUserInterfaceContract.EventListener listener);
        void updateSquare(int x, int y, int input);

        //Cập nhật toàn bộ bảng, chẳng hạn như sau khi hoàn thành trò chơi hoặc thực hiện chương trình ban đầu
        void updateBoard(SudokuGame game);
        void showDialog(String message);
        void showError(String message);
    }
}
