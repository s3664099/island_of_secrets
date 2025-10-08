package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import command_process.ParsedCommand;
import data.GameEntities;

/**
 * Unit tests for {@link ParsedCommand}.
 */
public class ParsedCommandTest {

    private static final String[] TWO_WORDS = {"take", "sword"};
    private static final String[] ONE_WORD = {"north"};

    @BeforeAll
    static void init() {
        // Nothing needed — just verifying constants compile
    }

    @Test
    void testConstructorAndGetters() {
        ParsedCommand cmd = new ParsedCommand(1, 2, "coded", TWO_WORDS, "take sword");

        assertEquals(1, cmd.getVerbNumber());
        assertEquals(2, cmd.getNounNumber());
        assertEquals("coded", cmd.getCodedCommand());
        assertEquals("take sword", cmd.getCommand());
        assertArrayEquals(TWO_WORDS, cmd.getSplitTwoCommand());
        assertArrayEquals(new String[]{"take", "sword"}, cmd.getSplitFullCommand());
    }

    @Test
    void testCheckNounLength_WithNoun() {
        ParsedCommand cmd = new ParsedCommand(0, 0, "", TWO_WORDS, "take sword");
        assertTrue(cmd.checkNounLength());
    }

    @Test
    void testCheckNounLength_NoNoun() {
        ParsedCommand cmd = new ParsedCommand(0, 0, "", ONE_WORD, "north");
        assertFalse(cmd.checkNounLength());
    }

    @Test
    void testMoveCommand() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_NORTH, 0, "", ONE_WORD, "north");
        assertTrue(cmd.checkMoveState());
    }

    @Test
    void testSingleCommand_Eat() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_EAT, 0, "", ONE_WORD, "eat");
        assertTrue(cmd.checkSingleCommandState());
        assertTrue(cmd.checkEat());
    }

    @Test
    void testSingleCommand_Drink() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_DRINK, 0, "", ONE_WORD, "drink");
        assertTrue(cmd.checkSingleCommandState());
        assertTrue(cmd.checkDrink());
    }

    @Test
    void testMultipleCommand_Take() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_TAKE, 0, "", TWO_WORDS, "take sword");
        assertTrue(cmd.checkMultipleCommandState());
        assertTrue(cmd.checkTake());
    }

    @Test
    void testMultipleCommand_Drop() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_DROP, 0, "", TWO_WORDS, "drop sword");
        assertTrue(cmd.checkDrop());
    }

    @Test
    void testMultipleCommand_Attack() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.ATTACK_BOTTOM + 1, 0, "", TWO_WORDS, "attack monster");
        assertTrue(cmd.checkMultipleCommandState());
        assertTrue(cmd.checkAttack());
    }

    @Test
    void testSingleCommand_Quit() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_QUIT, 0, "", ONE_WORD, "quit");
        assertTrue(cmd.checkSingleCommandState());
        assertTrue(cmd.checkQuit());
    }

    @Test
    void testUpdateStateChangesType() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_TAKE, 0, "", TWO_WORDS, "take sword");
        assertTrue(cmd.checkTake());

        cmd.updateState(GameEntities.CMD_EAT);
        assertTrue(cmd.checkEat());
    }

    @Test
    void testChopAndKillCommands() {
        ParsedCommand chopCmd = new ParsedCommand(GameEntities.CHOP_BOTTOM + 1, 0, "", TWO_WORDS, "chop tree");
        ParsedCommand killCmd = new ParsedCommand(GameEntities.CMD_KILL, 0, "", TWO_WORDS, "kill goblin");

        assertTrue(chopCmd.checkChop());
        assertTrue(killCmd.checkKill());
    }

    @Test
    void testSayCommand() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_SAY, 0, "", TWO_WORDS, "say hello");
        assertTrue(cmd.checkSay());
    }

    @Test
    void testRestCommand() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_WAIT, 0, "", ONE_WORD, "wait");
        assertTrue(cmd.checkRest());
    }

    @Test
    void testExamineCommand() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_EXAMINE, 0, "", TWO_WORDS, "examine rock");
        assertTrue(cmd.checkExamine());
    }

    @Test
    void testShelterCommand() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_SHELTER, 0, "", ONE_WORD, "shelter");
        assertTrue(cmd.checkShelter());
    }

    @Test
    void testSwimCommand() {
        ParsedCommand cmd = new ParsedCommand(GameEntities.CMD_SWIM, 0, "", ONE_WORD, "swim");
        assertTrue(cmd.checkSwim());
    }
}

