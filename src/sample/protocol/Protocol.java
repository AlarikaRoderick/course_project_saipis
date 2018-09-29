package sample.protocol;

import sample.User.User;

import java.io.IOException;
import java.io.InputStream;

public class Protocol {
    public static byte[] serialize(User user){
        byte[] userLoginSerialized = serializeString(user.getUserLogin());
        byte[] userPasswordSerialized = serializeString(user.getUserPassword());
        //byte[] userExist = serializeString(user.getExist());

        int packageLength = userLoginSerialized.length + userPasswordSerialized.length;

        byte[] length = getBytes(packageLength);
        byte[] data = new byte[4+packageLength+1];

        data[0] = length[0];
        data[1] = length[1];
        data[2] = length[2];
        data[3] = length[3];

        System.arraycopy(userLoginSerialized, 0, data, 4, userLoginSerialized.length);
        System.arraycopy(userPasswordSerialized, 0, data, 4 + userLoginSerialized.length, userPasswordSerialized.length);
        //System.arraycopy(userExist, 0, data, 4+userLoginSerialized.length+userPasswordSerialized.length, userExist.length);

        data[data.length-1] = getCrc(data, 4, data.length-1);

        return data;
    }

    public static byte getCrc(byte[] data, int start, int stop) {
        byte crc = 0;
        for (int i = start; i < stop; i++) {
            crc = (byte) (crc + data[i]);
        }
        return crc;
    }

    public static byte[] getBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i>>24);
        result[1] = (byte) (i>>16);
        result[2] = (byte) (i>>8);
        result[3] = (byte) (i);

        return result;
    }

    public static byte[] serializeString(String str) {
        byte[] strMass = new byte[4+str.getBytes().length];
        byte[] length = getBytes(str.getBytes().length);

        strMass[0] = length[0];
        strMass[1] = length[1];
        strMass[2] = length[2];
        strMass[3] = length[3];

        System.arraycopy(str.getBytes(), 0, strMass, 4, str.getBytes().length);
        return strMass;
    }

    public static User deserialize(InputStream inputStream) throws IOException{
        byte[] length = new byte[4];
        inputStream.read(length);
        byte[] payLoad = new byte[Protocol.byteToInt(length)];
        inputStream.read(payLoad);

        byte[] crc = new byte[1];
        inputStream.read(crc);

        return deserialize(payLoad);
    }

    public static User deserialize(byte[] payLoad){
        User user = new User();

        int userLoginSerializedLength = bytesToInt(payLoad[0], payLoad[1], payLoad[2], payLoad[3]);
        byte[] userLoginSerializedData = new byte[userLoginSerializedLength];

        System.arraycopy(payLoad, 4, userLoginSerializedData, 0, userLoginSerializedLength);

        int userPasswordSerializedLength = bytesToInt(payLoad[4+userLoginSerializedLength],
                payLoad[4+userLoginSerializedLength+1],
                payLoad[4+userLoginSerializedLength+2],
                payLoad[4+userLoginSerializedLength+3]);

        byte[] userPasswordSerializedData = new byte[userPasswordSerializedLength];

        System.arraycopy(payLoad, 4+4+userLoginSerializedLength, userPasswordSerializedData, 0, userPasswordSerializedLength);

        /*int userExistLength = bytesToInt(payLoad[4+userLoginSerializedLength+userPasswordSerializedLength],
                payLoad[4+userLoginSerializedLength+userPasswordSerializedLength + 1],
                payLoad[4+userLoginSerializedLength+userPasswordSerializedLength + 2],
                payLoad[4+userLoginSerializedLength+userPasswordSerializedLength + 3]);

        byte[] userExistData = new byte[userExistLength];

        System.arraycopy(payLoad, 4+4+4+userLoginSerializedLength+userPasswordSerializedLength, userExistData, 0, userExistLength);*/

        user.setUserLogin(new String(userLoginSerializedData));
        user.setUserPassword(new String(userPasswordSerializedData));
        //user.setExist(new String(userExistData));

        return user;
    }

    public static int bytesToInt(byte b0, byte b1, byte b2, byte b3) {
        return b3 & 0xFF | (b2 & 0xFF) << 8 | (b1 & 0xFF) << 16 | (b0 & 0xFF) << 24;
    }

    private static int byteToInt(byte[] length) {
        return length[3] & 0xFF | (length[2] & 0xFF) << 8 | (length[1] & 0xFF) << 16 | (length[0] & 0xFF) << 24;
    }
}
