/*
Title: Island of Secrets Move Command
Author: Jenny Tyler & Les Howarth
Translator: David Sarkies
Version: 4.0
Date: 5 October 2025
Source: https://archive.org/details/island-of-secrets_202303
*/

package Tests;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import command_process.ActionResult;
import command_process.ParsedCommand;
import commands.Combat;
import data.GameEntities;
import data.Item;
import game.Game;
import game.Player;

class CombatTest {

    private Game mockGame;
    private Player mockPlayer;
    private ParsedCommand mockCommand;
    private Combat combat;

    @BeforeEach
    void setUp() {
        mockGame = mock(Game.class);
        mockPlayer = mock(Player.class);
        mockCommand = mock(ParsedCommand.class);

        when(mockCommand.getCodedCommand()).thenReturn("");
        when(mockCommand.getVerbNumber()).thenReturn(0);
        when(mockCommand.getNounNumber()).thenReturn(0);

        combat = new Combat(mockGame, mockPlayer, mockCommand);
    }

    @Test
    void testConstructorInitializesFields() {
        assertNotNull(combat);
    }
    
    @Test
    void testChop_NoWeapon_NothingHappens() {
        ActionResult result = combat.chop();

        verify(mockGame).addMessage("Nothing happens", true, true);
        assertFalse(result.isValid(), "Default chop() without weapon should fail");
    }
    
    @Test
    void testChop_ChoppingRootsWithAxe() {
        when(mockCommand.getCodedCommand()).thenReturn(GameEntities.CODE_CHOPPING_ROOTS);
        when(mockGame.getItem(GameEntities.ITEM_AXE)).thenReturn(mock(Item.class));

        Item axe = mock(Item.class);
        when(mockGame.getItem(GameEntities.ITEM_AXE)).thenReturn(axe);
        when(axe.getItemLocation()).thenReturn(GameEntities.ROOM_CARRYING);

        combat = new Combat(mockGame, mockPlayer, mockCommand);
        ActionResult result = combat.chop();

        assertTrue(result.isValid(), "Chopping roots with an axe should succeed");
    }

    @Test
    void testAttack_HitOmegan() {
        when(mockCommand.getNounNumber()).thenReturn(GameEntities.ITEM_OMEGAN);
        when(mockGame.getItem(GameEntities.ITEM_OMEGAN)).thenReturn(mock(Item.class));
        when(mockGame.getItem(GameEntities.ITEM_OMEGAN).getItemLocation()).thenReturn(GameEntities.ROOM_CARRYING);

        combat = new Combat(mockGame, mockPlayer, mockCommand);
        ActionResult result = combat.attack();

        verify(mockGame).addMessage("He laughs dangerously.", true, true);
        assertTrue(result.isValid());
    }

    @Test
    void testKill_FatalResponse() {
        Item mockTarget = mock(Item.class);
        when(mockCommand.getNounNumber()).thenReturn(5);
        when(mockGame.getItem(5)).thenReturn(mockTarget);
        when(mockTarget.getItemLocation()).thenReturn(10);
        when(mockPlayer.getRoom()).thenReturn(10);

        ActionResult result = combat.kill();

        assertTrue(result.isValid());
        verify(mockGame).setEndGameState();
    }
    
    @Test
    void testStrikeFlint_NoCoal() {
        when(mockCommand.getCodedCommand()).thenReturn(GameEntities.CODE_HAS_FLINT);
        when(mockPlayer.getRoom()).thenReturn(1);
        when(mockGame.getItem(GameEntities.ITEM_COAL)).thenReturn(mock(Item.class));
        when(mockGame.getItem(GameEntities.ITEM_COAL).getItemLocation()).thenReturn(2); // not same room

        ActionResult result = combat.attack();

        verify(mockGame).addMessage("Sparks fly", true, true);
        assertTrue(result.isValid());
    }

    @Test
    void testStrikeFlint_WithCoal() {
        when(mockCommand.getCodedCommand()).thenReturn(GameEntities.CODE_HAS_FLINT);
        when(mockPlayer.getRoom()).thenReturn(5);
        when(mockGame.getItem(GameEntities.ITEM_COAL)).thenReturn(mock(Item.class));
        when(mockGame.getItem(GameEntities.ITEM_COAL).getItemLocation()).thenReturn(5); // same room

        ActionResult result = combat.attack();

        verify(mockGame, atLeastOnce()).addPanelMessage(anyString(), anyBoolean());
        assertTrue(result.isValid());
    }
    
    /*
    @Test
    void testReduceStats_DecreasesPlayerStats() {
        when(mockPlayer.getStat(Combat.STAT_STRENGTH)).thenReturn(50f);
        when(mockPlayer.getStat(Combat.STAT_WISDOM)).thenReturn(30);

        // use reflection since reduceStats() is private
        try {
            var method = Combat.class.getDeclaredMethod("reduceStats", int.class, int.class);
            method.setAccessible(true);
            method.invoke(combat, 5, 10);

            verify(mockPlayer).setStat(Combat.STAT_STRENGTH, 45f);
            verify(mockPlayer).setStat(Combat.STAT_WISDOM, 20);
        } catch (Exception e) {
            fail(e);
        }
    }
    */
}

/* 5 October 2025 - Created test file
 * 
 */

