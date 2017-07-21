package innopolis.nikbird.org.iceandfireuniverse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import innopolis.nikbird.org.iceandfireuniverse.models.Character;

/**
 * Created by nikbird on 21.07.17.
 */

public class ManagerCharacters {

    private final static ManagerCharacters INSTANCE = new ManagerCharacters();
    public final static ManagerCharacters getInstance() {
        return INSTANCE;
    }

    private static String sBaseUrlString = "https://www.anapioficeandfire.com/api/characters";

    private URL mBaseUrl;
    private Set<Character> mCharacters = new HashSet<>(50);

    private ManagerCharacters() {
    }

    public void setUrl(String urlString) throws MalformedURLException {
        mBaseUrl = new URL(urlString);
    }




}
