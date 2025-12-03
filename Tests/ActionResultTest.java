/*
Title: Island of Secrets Action Result Junit tests
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 5.0
Date: 3 December 2025
Source: https://archive.org/details/island-of-secrets_202303 

- Update methods calling class
*/

package Tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import game.Game;
import game.Player;
import command_process.ActionResult;

public class ActionResultTest {

    private Game mockGame;
    private Player mockPlayer;

    @Test
    void testDefaultConstructor() {
        ActionResult result = new ActionResult();

        assertNull(result.getGame(), "Default constructor should have null game");
        assertNull(result.getPlayer(), "Default constructor should have null player");
        assertFalse(result.isValid(), "Default constructor should be invalid");
    }

    @Test
    void testParameterizedConstructor_ValidTrue() {
        ActionResult result = new ActionResult(mockGame, mockPlayer, true);

        assertSame(mockGame, result.getGame(), "Game should be the one passed in constructor");
        assertSame(mockPlayer, result.getPlayer(), "Player should be the one passed in constructor");
        assertTrue(result.isValid(), "Result should be valid");
    }

    @Test
    void testParameterizedConstructor_ValidFalse() {
        ActionResult result = new ActionResult(mockGame, mockPlayer, false);

        assertSame(mockGame, result.getGame());
        assertSame(mockPlayer, result.getPlayer());
        assertFalse(result.isValid());
    }

    @Test
    void testSuccessFactoryMethod() {
        ActionResult base = new ActionResult(); // base instance not important
        ActionResult result = base.success(mockGame, mockPlayer);

        assertSame(mockGame, result.getGame());
        assertSame(mockPlayer, result.getPlayer());
        assertTrue(result.isValid());
    }

    @Test
    void testFailureFactoryMethod() {
        ActionResult base = new ActionResult();
        ActionResult result = base.failure(mockGame, mockPlayer);

        assertSame(mockGame, result.getGame());
        assertSame(mockPlayer, result.getPlayer());
        assertFalse(result.isValid());
    }

    @Test
    void testToStringContainsInfo() {
        ActionResult result = new ActionResult(mockGame, mockPlayer, true);
        String str = result.toString();

        assertTrue(str.contains("game="), "toString should contain 'game='");
        assertTrue(str.contains("player="), "toString should contain 'player='");
        assertTrue(str.contains("valid=true"), "toString should indicate valid=true");
    }
}
/*
 * 3 December 2025 - Increased version number
 */

