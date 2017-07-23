package innopolis.nikbird.org.iceandfireuniverse;

import android.content.Context;
import android.os.Parcel;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.JsonReader;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.ICharacter;
import innopolis.nikbird.org.iceandfireuniverse.models.Character;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class CharacterModelTest {

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("innopolis.nikbird.org.iceandfireuniverse", appContext.getPackageName());
    }

    @Test
    public void characterCreationJson_isCorrect() throws IOException, Character.MissedURLException, Character.MissedIdException {
        ICharacter character = createJsonCharacter(makeJsonString());

        assertEquals(character.getUrl(), "https://www.anapioficeandfire.com/api/characters/1");
        assertEquals(character.getName(), "CharName");
        assertEquals(character.getCulture(), "Braavosi");
        assertEquals(character.getBorn(), "in 1967");
        assertEquals(character.getDied(), "in 2016");
        assertEquals(character.getTitles()[0], "The Lord");
        assertEquals(character.getTitles()[1], "The King");
        assertEquals(character.getAliases()[0], "Hodor");
        assertEquals(character.getAliases()[1], "Батяня");
    }

    @Test
    public void characterParcellable_isCorrect() throws IOException, Character.MissedURLException, Character.MissedIdException {
        ICharacter character = createJsonCharacter(makeJsonString());

        Parcel parcel = Parcel.obtain();
        character.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        ICharacter charFromParcel = Character.CREATOR.createFromParcel(parcel);
        assertEquals(character, charFromParcel);
        assertEquals(character.getUrl(), charFromParcel.getUrl());
        assertEquals(character.getName(), charFromParcel.getName());
        assertEquals(character.getCulture(), charFromParcel.getCulture());
        assertEquals(character.getBorn(), charFromParcel.getBorn());
        assertEquals(character.getDied(), charFromParcel.getDied());
        assertEquals(character.getTitles()[0], charFromParcel.getTitles()[0]);
        assertEquals(character.getTitles()[1], charFromParcel.getTitles()[1]);
        assertEquals(character.getAliases()[0], charFromParcel.getAliases()[0]);
        assertEquals(character.getAliases()[1], charFromParcel.getAliases()[1]);

    }

    private ICharacter createJsonCharacter(String json) throws IOException, Character.MissedURLException, Character.MissedIdException {
        InputStream in = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        ICharacter character = new Character(reader);
        return character;
    }

    private String makeJsonString() {
        StringBuilder jsonTestString = new StringBuilder();
        jsonTestString
                .append("{")
                .append("  \"url\": \"https://www.anapioficeandfire.com/api/characters/1\",")
                .append("  \"name\": \"CharName\",")
                .append("  \"culture\": \"Braavosi\",")
                .append("  \"born\": \"in 1967\",")
                .append("  \"died\": \"in 2016\",")
                .append("  \"titles\": [")
                .append("    \"The Lord\",")
                .append("    \"The King\"")
                .append("  ],")
                .append("  \"aliases\": [")
                .append("    \"Hodor\",")
                .append("    \"Батяня\"")
                .append("  ]")
                .append("}");
        return jsonTestString.toString();
    }
}
