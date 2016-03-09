package asap.rsbembodiments;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.hamcrest.collection.IsIterableContainingInOrder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableList;

/**
 * Unit tests for the RsbMorphFaceController
 * @author hvanwelbergen
 *
 */
public class RsbMorphFaceControllerTest
{
    private RsbEmbodiment mockRsbEmbodiment = mock(RsbEmbodiment.class);
    private BiMap<String,String> morphMap = HashBiMap.create();
    
    @Before
    public void setup()
    {
        when(mockRsbEmbodiment.getAvailableMorphs()).thenReturn(ImmutableList.of("face1","face2","face3"));
        morphMap.put("face1","face1map");
        morphMap.put("face2","face2map");
        morphMap.put("face3","face3map");
    }
    
    @Test
    public void testPossibleMorphs()
    {
        RsbMorphFaceController mfc = new RsbMorphFaceController("billie", mockRsbEmbodiment);
        assertThat(mfc.getPossibleFaceMorphTargetNames(),IsIterableContainingInAnyOrder.containsInAnyOrder("face1","face2","face3")); 
    }
    
    @Test
    public void testPossibleMorphsWithMap()
    {
        RsbMorphFaceController mfc = new RsbMorphFaceController("billie", mockRsbEmbodiment,morphMap);
        assertThat(mfc.getPossibleFaceMorphTargetNames(),IsIterableContainingInAnyOrder.containsInAnyOrder("face1map","face2map","face3map")); 
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSelection()
    {
        RsbMorphFaceController mfc = new RsbMorphFaceController("billie", mockRsbEmbodiment);
        mfc.initialize();   
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> args = ArgumentCaptor.forClass(List.class);
        verify(mockRsbEmbodiment).selectMorphs(args.capture());
        assertThat((List<String>)args.getValue(), IsIterableContainingInOrder.contains("face1","face2","face3"));
    }
    
    @SuppressWarnings("unchecked")
    @Test
    public void testSelectionWithMapping()
    {
        RsbMorphFaceController mfc = new RsbMorphFaceController("billie", mockRsbEmbodiment, morphMap);
        mfc.initialize();   
        @SuppressWarnings("rawtypes")
        ArgumentCaptor<List> args = ArgumentCaptor.forClass(List.class);
        verify(mockRsbEmbodiment).selectMorphs(args.capture());
        assertThat((List<String>)args.getValue(), IsIterableContainingInOrder.contains("face1","face2","face3"));
    }
    @Test
    public void testGetMorphValues()
    {
        RsbMorphFaceController mfc = new RsbMorphFaceController("billie", mockRsbEmbodiment);
        mfc.initialize();
        mfc.addMorphTargets(new String[]{"face1","face2","face3"}, new float[]{0.1f,0.2f,0.3f});
        assertThat(mfc.getMorphValues(), IsIterableContainingInOrder.contains(0.1f,0.2f,0.3f));
    }    
}
