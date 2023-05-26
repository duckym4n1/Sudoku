package buildlogic;
import computationlogic.GameLogic;
import persistence.LocalStorageImpl;
import problemdomain.IStorage;
import problemdomain.SudokuGame;
import userinterface.IUserInterfaceContract;
import userinterface.logic.ControlLogic;

import java.io.IOException;

public class SudokuBuildLogic {
    /**
     * Lớp này lấy đối tượng uiImpl được kết hợp chặt chẽ với khung JavaFX,
     * và ràng buộc đối tượng đó với các đối tượng khác nhau cần thiết cho ứng dụng hoạt động.
     */
    public static void build(IUserInterfaceContract.View userInterface) throws IOException {
        SudokuGame initialState;
        IStorage storage = new LocalStorageImpl();

        try {

            initialState = storage.getGameData();
        } catch (IOException e) {

            initialState = GameLogic.getNewGame();
            storage.updateGameData(initialState);
        }

        IUserInterfaceContract.EventListener uiLogic = new ControlLogic(storage, userInterface);
        userInterface.setListener(uiLogic);
        userInterface.updateBoard(initialState);
    }
}
