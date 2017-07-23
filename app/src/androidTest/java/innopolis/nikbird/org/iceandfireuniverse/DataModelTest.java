package innopolis.nikbird.org.iceandfireuniverse;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import innopolis.nikbird.org.iceandfireuniverse.interfaces.IDataModel;
import innopolis.nikbird.org.iceandfireuniverse.models.DataModel;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by nikbird on 24.07.17.
 */

@RunWith(AndroidJUnit4.class)
public class DataModelTest {

    @Test
    public void dataModel_isCorrect() throws Exception {

        IDataModel dataModel;
        IDataModel.IListener viewModel = mock(IDataModel.IListener.class);

        /**
         *  После создания модели, она должна создать новый поток и в нем вызвать
         *  метод onModelReady. Это может занять некоторое время, поэтому немного подождем.
         */
        dataModel = new DataModel(viewModel);
        Thread.sleep(200);
        verify(viewModel, times(1)).onModelReady();

        /**
         *  После запроса данных, модель делает http(s)-запрос, поэтому нужно
         *  внести небольшую задержку
         */
        dataModel.requestData(0, new IDataModel.PageRequest(1, 20));
        Thread.sleep(2000);
        verify(viewModel, times(1)).onDataReady(anyInt(), any(List.class));

        dataModel.requestData(0, new IDataModel.PageRequest(1, 0));
        Thread.sleep(2000);
        verify(viewModel, times(1)).onDataFail(anyInt(), anyString());
    }

}
