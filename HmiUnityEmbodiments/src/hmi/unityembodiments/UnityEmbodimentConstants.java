package hmi.unityembodiments;

/**
 * Constants for the protocol between ASAP and Unity.
 * @author jankolkmeier@gmail.com
 */
public final class UnityEmbodimentConstants
{
    private UnityEmbodimentConstants()
    {

    }

    public static final String JSON_MSG_BINARY = "binaryMessage";
    public static final String JSON_MSG_WORLDUPDATE = "worldUpdate";
    public static final String JSON_MSG_WORLDUPDATE_CONTENT = "objects";
    public static final String JSON_MSG_BINARY_CONTENT = "content";

    public static final String AUPROT_PROP_MSGTYPE = "msgType";
    public static final String AUPROT_PROP_AGENTID = "agentId";
    public static final String AUPROT_PROP_SOURCE = "source";
    public static final String AUPROT_PROP_N_BONES = "nBones";
    public static final String AUPROT_PROP_N_FACETARGETS = "nFaceTargets";
    public static final String AUPROT_PROP_N_OBJECTS = "nObjects";
    public static final String AUPROT_PROP_BONES = "bones";
    public static final String AUPROT_PROP_BONE_VALUES = "boneValues";
    public static final String AUPROT_PROP_BINARY_BONE_VALUES = "binaryBoneValues";
    public static final String AUPROT_PROP_FACETARGETS = "faceTargets";
    public static final String AUPROT_PROP_FACETARGET_VALUES = "faceTargetValues";
    public static final String AUPROT_PROP_BINARY_FACETARGET_VALUES = "binaryFaceTargetValues";
    public static final String AUPROT_PROP_OBJECTS = "objects";
    public static final String AUPROT_PROP_OBJECTS_BINARY = "objectsBinary";

    public static final String AUPROT_PROP_BONE_ID = "boneId";
    public static final String AUPROT_PROP_BONE_PARENTID = "parentId";
    public static final String AUPROT_PROP_BONE_HANIMNAME = "hAnimName";
    public static final String AUPROT_PROP_TRANSFORM = "transform";
    public static final String AUPROT_PROP_OBJECT_ID = "objectId";
    public static final String AUPROT_PROP_SUBTITLE = "subtitle";

    public static final String AUPROT_MSGTYPE_AGENTSPECREQUEST = "AgentSpecRequest";
    public static final String AUPROT_MSGTYPE_AGENTSPEC = "AgentSpec";
    public static final String AUPROT_MSGTYPE_AGENTSTATE = "AgentState";
    public static final String AUPROT_MSGTYPE_WORLDOBJECTUPDATE = "WorldObjectUpdate";

    /*
     * TODO: These AVATAR specific constants need to be moved to the respective class in the AVATAR project.
     */
    public static final String AUPROT_PROP_STATE = "state";
    public static final String AUPROT_PROP_MESSAGE = "message";
    public static final String AUPROT_MESSAGE_APPLICATION_STATE = "ApplicationState";
    public static final String AUPROT_MSGTYPE_SHOWSUBTITLES = "ShowSubtitle";
    public static final String AUPROT_MSGTYPE_HIDESUBTITLES = "HideSubtitle";

}
