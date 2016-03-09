package hmi.ipaacarenderenvironment;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContaining;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.doAnswer;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import hmi.animation.VJoint;
import hmi.faceanimation.FaceController;
import hmi.math.Mat4f;
import hmi.math.Quat4f;
import hmi.math.Vec3f;
import hmi.testutil.math.Quat4fTestUtil;
import hmi.testutil.math.Vec3fTestUtil;
import ipaaca.AbstractIU;
import ipaaca.IUEventHandler;
import ipaaca.IUEventType;
import ipaaca.InputBuffer;
import ipaaca.LocalIU;
import ipaaca.OutputBuffer;
import ipaaca.Payload;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.AdditionalMatchers;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.common.collect.ImmutableList;
/**
 * Unit test for the IpaacaRenderConnector
 * @author Herwin
 * 
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(IpaacaRenderConnector.class)
public class IpaacaRenderConnectorTest
{
    private FaceController mockFaceController = mock(FaceController.class);
    private VJoint joint1 = new VJoint();
    private VJoint joint2 = new VJoint();
    private VJoint joint3 = new VJoint();
    private InputBuffer mockInBuffer = mock(InputBuffer.class);
    private List<IUEventHandler> handlers = new ArrayList<>();
    private AbstractIU mockIUData = mock(AbstractIU.class);
    private Payload mockPayloadData = mock(Payload.class);
    private AbstractIU mockIUConfig = mock(AbstractIU.class);
    private Payload mockPayloadConfig = mock(Payload.class);
    private OutputBuffer mockOutBuffer = mock(OutputBuffer.class);
    private static final float PRECISION = 0.001f;
    private IpaacaRenderConnector connector;

    private final float[]JOINT1_TRANS = {1,2,3};
    private final float[]JOINT2_TRANS = {4,5,6};
    private final float[]JOINT3_TRANS = {7,8,9};
    private final float JOINT1_ROT[]={1,0,0,0};
    private final float JOINT2_ROT[]={0,1,0,0};
    private final float JOINT3_ROT[]={0,0.707f,0.707f,0};
    
    
    @Before
    public void setup() throws Exception
    {
        when(mockFaceController.getPossibleFaceMorphTargetNames()).thenReturn(ImmutableList.of("morph1,morph2,morph3"));
        whenNew(InputBuffer.class).withArguments(anyString(), any(Set.class)).thenReturn(mockInBuffer);
        whenNew(OutputBuffer.class).withArguments(anyString()).thenReturn(mockOutBuffer);

        joint1.setSid("joint1");
        joint2.setSid("joint2");
        joint3.setSid("joint3");
        joint1.addChild(joint2);
        joint2.addChild(joint3);
        joint1.setTranslation(JOINT1_TRANS);
        joint2.setTranslation(JOINT2_TRANS);
        joint3.setTranslation(JOINT3_TRANS);
        joint1.setRotation(JOINT1_ROT);
        joint2.setRotation(JOINT2_ROT);
        joint3.setRotation(JOINT3_ROT);
        
        connector = new IpaacaRenderConnector("id1", mockFaceController, joint1);

        doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                IUEventHandler handler = (IUEventHandler) (invocation.getArguments()[0]);
                handlers.add(handler);
                return null;
            }
        }).when(mockInBuffer).registerHandler(any(IUEventHandler.class));
    }

    private void sendNotify(String state)
    {
        AbstractIU mockIUNotify = mock(AbstractIU.class);
        Payload mockNotifyPayload = mock(Payload.class);
        when(mockNotifyPayload.get("recv_categories")).thenReturn("jointDataConfigRequest, componentNotify");
        when(mockIUNotify.getCategory()).thenReturn("componentNotify");
        when(mockIUNotify.getPayload()).thenReturn(mockNotifyPayload);
        when(mockInBuffer.getIU("iuNotify")).thenReturn(mockIUNotify);
        when(mockNotifyPayload.get("state")).thenReturn(state);
        
        for (IUEventHandler handler : handlers)
        {
            handler.call(mockInBuffer, "iuNotify", false, IUEventType.ADDED, "componentNotify");
        }
    }
    
    private float[] parseFloatArray(String str)
    {
        String floats[] = str.split("\\s+");
        float [] res = new float[floats.length];
        for(int i=0;i<floats.length;i++)
        {
            res[i]=Float.parseFloat(floats[i]);
        }
        return res;
    }
    
    private void verifyJointDataConfigRequestIU(LocalIU iu)
    {
        assertEquals("jointDataConfigRequest", iu.getCategory());
        String morphs = iu.getPayload().get("morphs");
        String joints = iu.getPayload().get("joints");
        String parentJoints = iu.getPayload().get("joint_parents");
        String translations[] = iu.getPayload().get("joint_translations").split("\\s*,\\s*");
        String rotations[] = iu.getPayload().get("joint_rotations").split("\\s*,\\s*");
        
        assertThat(morphs.split("\\s*,\\s*"), arrayContaining("morph1","morph2","morph3"));
        assertThat(joints.split("\\s*,\\s*"), arrayContaining("joint1","joint2","joint3"));
        assertThat(parentJoints.split("\\s*,\\s*"), arrayContaining("-","joint1","joint2"));        
        Vec3fTestUtil.assertVec3fEquals(JOINT1_TRANS, parseFloatArray(translations[0]), PRECISION);
        Vec3fTestUtil.assertVec3fEquals(JOINT2_TRANS, parseFloatArray(translations[1]), PRECISION);
        Vec3fTestUtil.assertVec3fEquals(JOINT3_TRANS, parseFloatArray(translations[2]), PRECISION);
        Quat4fTestUtil.assertQuat4fEquals(JOINT1_ROT, parseFloatArray(rotations[0]),PRECISION);
        Quat4fTestUtil.assertQuat4fEquals(JOINT2_ROT, parseFloatArray(rotations[1]),PRECISION);
        Quat4fTestUtil.assertQuat4fEquals(JOINT3_ROT, parseFloatArray(rotations[2]),PRECISION);
    }
    
    @Test
    public void testNotifyAndJointDataConfigRequestAtInit()
    {
        connector.initialize();
        ArgumentCaptor<LocalIU> argument = ArgumentCaptor.forClass(LocalIU.class);
        verify(mockOutBuffer,times(2)).add(argument.capture());
        LocalIU iu = argument.getAllValues().get(0);
        assertEquals("componentNotify", iu.getCategory());
        assertEquals("new", iu.getPayload().get("state"));
        
        verifyJointDataConfigRequestIU(argument.getAllValues().get(1));        
    }

    
    
    @Test
    public void testNotifyAndJointDataConfigRequestAtNotifyNew()
    {
        connector.initialize();
        sendNotify("new");        
        ArgumentCaptor<LocalIU> argument = ArgumentCaptor.forClass(LocalIU.class);
        verify(mockOutBuffer,times(4)).add(argument.capture());
        LocalIU iu = argument.getAllValues().get(2);
        assertEquals("componentNotify", iu.getCategory());
        assertEquals("old", iu.getPayload().get("state"));
        
        verifyJointDataConfigRequestIU(argument.getAllValues().get(3));        
    }
    
    @Test
    public void testNoNotifyAtNotifyOld()
    {
        connector.initialize();
        sendNotify("old");        
        ArgumentCaptor<LocalIU> argument = ArgumentCaptor.forClass(LocalIU.class);
        verify(mockOutBuffer,times(2)).add(argument.capture());        
    }

    @Test
    public void test() throws Exception
    {

        connector.initialize();
        when(mockInBuffer.getIU("iu1")).thenReturn(mockIUData);
        when(mockInBuffer.getIU("iu2")).thenReturn(mockIUConfig);
        when(mockIUData.getPayload()).thenReturn(mockPayloadData);
        when(mockIUConfig.getPayload()).thenReturn(mockPayloadConfig);
        when(mockPayloadData.get("morph_data")).thenReturn("0.1 0.2 0.3");
        when(mockIUData.getPayload()).thenReturn(mockPayloadData);
        when(mockPayloadConfig.get("morphs_provided")).thenReturn("morph1, morph2, morph3");
        when(mockPayloadConfig.get("joints_provided")).thenReturn("joint1, joint2, joint3");
        when(mockPayloadData.get("morph_data")).thenReturn("10 20 30");
        float q1[] = Quat4f.getQuat4f();
        Quat4f.setFromAxisAngle4f(q1, 1, 0, 0, 0.5f);
        float q2[] = Quat4f.getQuat4f();
        Quat4f.setFromAxisAngle4f(q2, 1, 0, 0, 0.6f);
        float q3[] = Quat4f.getQuat4f();
        Quat4f.setFromAxisAngle4f(q3, 1, 0, 0, 0.7f);
        float m1[] = Mat4f.getMat4f();
        Mat4f.setFromTR(m1, Vec3f.getVec3f(), q1);
        float m2[] = Mat4f.getMat4f();
        Mat4f.setFromTR(m2, Vec3f.getVec3f(), q2);
        float m3[] = Mat4f.getMat4f();
        Mat4f.setFromTR(m3, Vec3f.getVec3f(), q3);
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < 16; i++)
        {
            buf.append(m1[i] + " ");
        }
        for (int i = 0; i < 16; i++)
        {
            buf.append(Mat4f.getIdentity()[i] + " ");
        }
        for (int i = 0; i < 16; i++)
        {
            buf.append(m2[i] + " ");
        }
        for (int i = 0; i < 16; i++)
        {
            buf.append(Mat4f.getIdentity()[i] + " ");
        }
        for (int i = 0; i < 16; i++)
        {
            buf.append(m3[i] + " ");
        }
        for (int i = 0; i < 16; i++)
        {
            buf.append(Mat4f.getIdentity()[i] + " ");
        }

        when(mockPayloadData.get("joint_data")).thenReturn(buf.toString().trim());
        for (IUEventHandler handler : handlers)
        {
            handler.call(mockInBuffer, "iu2", false, IUEventType.ADDED, "jointDataConfigReply");
        }
        for (IUEventHandler handler : handlers)
        {
            handler.call(mockInBuffer, "iu1", false, IUEventType.ADDED, "jointData");
        }

        float values[] = { 0.1f, 0.2f, 0.3f };
        String targets[] = { "morph1", "morph2", "morph3" };
        verify(mockFaceController).setMorphTargets(targets, values);
        verify(mockFaceController).setMorphTargets(any(String[].class), AdditionalMatchers.aryEq(values));
        float q[] = Quat4f.getQuat4f();
        joint1.getRotation(q);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(q1, q, PRECISION);
        joint2.getRotation(q);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(q2, q, PRECISION);
        joint3.getRotation(q);
        Quat4fTestUtil.assertQuat4fRotationEquivalent(q3, q, PRECISION);
    }
}
