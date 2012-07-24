package gdw.network;

import java.nio.ByteBuffer;


public abstract class NetMessageType 
{
	public static final byte DeadReckoningMessageType = 0;
	public static final byte EntityBusMessageType = 1;//tunnelmessage
	public static final byte EntitySpawnMessageType = 2;
	public static final byte EntityDespawnMessageType = 3;
	public static final byte TimeSyncMessageType = 4;
	
	public abstract void fillInByteBuffer(ByteBuffer buf);
}
