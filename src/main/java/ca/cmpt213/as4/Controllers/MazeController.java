package ca.cmpt213.as4.Controllers;

import ca.cmpt213.as4.Model.GameManager;
import ca.cmpt213.as4.Model.Maze;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ca.cmpt213.as4.restapi.ApiGameWrapper;
import ca.cmpt213.as4.restapi.ApiBoardWrapper;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class MazeController {
    GameManager manager = new GameManager();
    private List<ApiGameWrapper> games = new ArrayList<>();
    private List<ApiBoardWrapper> boards = new ArrayList<>();
    private AtomicInteger nextId = new AtomicInteger();

    @GetMapping("/api/about")
    public String getMyName() {
        return "Jieung Park";
    }

    @GetMapping("/api/games")
    public List<ApiGameWrapper> getGameList() {
        return games;
    }

    @PostMapping("/api/games")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiGameWrapper makeNewGame() {
        GameManager newGame = new GameManager();
        Maze newMaze = new Maze();

        ApiGameWrapper newWrapper = ApiGameWrapper.makeFromGame(newGame, nextId.incrementAndGet());
        ApiBoardWrapper newBoard = ApiBoardWrapper.makeFromGame(newMaze);
        newGame.revealMouse(1, 1, newBoard);
        newBoard = ApiBoardWrapper.makeFromGame(newMaze);
        newGame.makeCheese(newBoard);
        boards.add(newBoard);
        games.add(newWrapper);
        return newWrapper;
    }

    @GetMapping("/api/games/{id}")
    public ApiGameWrapper getIdGame(@PathVariable("id") int gameId) {
        for (ApiGameWrapper game : games) {
            if (game.gameNumber == gameId) {
                return game;
            }
        }

        throw new IndexOutOfBoundsException();
    }

    @GetMapping("/api/games/{id}/board")
    public ApiBoardWrapper getIdBoard(@PathVariable("id") int gameId) {
        int boardNum = 0;
        for (ApiGameWrapper game : games) {
            if (game.gameNumber == gameId) {
                return boards.get(boardNum);
            }
            boardNum++;
        }

        throw new IndexOutOfBoundsException();
    }

    @PostMapping("/api/games/{id}/moves")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeNewMove(@PathVariable("id") int gameId, @RequestBody String move) {
        if (gameId > games.size()) {
            throw new IndexOutOfBoundsException();
        }

        ApiGameWrapper changeWrapper = getIdGame(gameId);
        ApiBoardWrapper changeBoard = getIdBoard(gameId);

        boolean mouseMoved = false;
        if (move.contains("MOVE_UP")) {
            if (manager.moveMouse("up", changeBoard, changeWrapper)) {
                changeBoard.mouseLocation.y--;
                manager.eatCheese(changeWrapper, changeBoard);
                mouseMoved = true;
            }
        } else if (move.contains("MOVE_DOWN")) {
            if (manager.moveMouse("down", changeBoard, changeWrapper)) {
                changeBoard.mouseLocation.y++;
                manager.eatCheese(changeWrapper, changeBoard);
                mouseMoved = true;
            }
        } else if (move.contains("MOVE_LEFT")) {
            if (manager.moveMouse("left", changeBoard, changeWrapper)) {
                changeBoard.mouseLocation.x--;
                manager.eatCheese(changeWrapper, changeBoard);
                mouseMoved = true;
            }
        } else if (move.contains("MOVE_RIGHT")) {
            if (manager.moveMouse("right", changeBoard, changeWrapper)) {
                changeBoard.mouseLocation.x++;
                manager.eatCheese(changeWrapper, changeBoard);
                mouseMoved = true;
            }
        }

        if (move.contains("MOVE_CATS") && mouseMoved) {
            manager.moveCat(changeBoard, changeWrapper);
        } else if (move.contains("MOVE_CATS") && !mouseMoved) {
            manager.moveCat(changeBoard, changeWrapper);
        } else if (!mouseMoved){
            throw new IllegalArgumentException();
        }

    }

    @PostMapping("/api/games/{id}/cheatstate")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void makeCheatState(@PathVariable("id") int gameId, @RequestBody String cheat) {
        if (gameId > games.size()) {
            throw new IndexOutOfBoundsException();
        }

        ApiGameWrapper changeWrapper = getIdGame(gameId);
        ApiBoardWrapper changeBoard = getIdBoard(gameId);
        GameManager manager = new GameManager();

        if (cheat.contains("1_CHEESE")) {
            changeWrapper.numCheeseGoal = 1;
        } else if (cheat.contains("SHOW_ALL")) {
            manager.showMap(changeBoard);
        } else {
            throw new IllegalArgumentException();
        }
    }

    @ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Request message not found")
    @ExceptionHandler(IllegalArgumentException.class)
    public void badMessageExceptionHandler() {
        //Nothing to do
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Request ID not found.")
    @ExceptionHandler(IndexOutOfBoundsException.class)
    public void badIDExceptionHandler() {
        //Nothing to do
    }
}
