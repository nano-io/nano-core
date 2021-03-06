package net.subnano.codec.wire;

import static net.subnano.codec.wire.WireType.BYTE_SIZE;
import static net.subnano.codec.wire.WireType.LONG_SIZE;
import static net.subnano.codec.wire.WireType.SHORT_SIZE;

/**
 * TODO read buffer header
 */
public class NanoDecoder {

    public void parse(BufferReader bufferReader, WireVisitor visitor) {
        // TODO read buffer header
        int offset = 0;
        int len;
        while (offset <= bufferReader.position()) {
            byte type = bufferReader.readByte(offset++);
            switch (type) {
                case WireType.BYTE:
                case WireType.SHORT:
                case WireType.INT:
                case WireType.LONG:
                case WireType.DOUBLE:
                    int valueLen = WireType.sizeOf(type);
                    visitor.onTag(bufferReader, type, offset, valueLen);
                    offset += valueLen;
                    break;
                case WireType.STRING:
                    len = bufferReader.readByte(offset) & 0xff;
                    visitor.onTag(bufferReader, type, offset + 1, len);
                    offset += (BYTE_SIZE + len);
                    break;
                case WireType.TEXT:
                    len = bufferReader.readShort(offset) & 0xffff;
                    visitor.onTag(bufferReader, type, offset + 2, len);
                    offset += (SHORT_SIZE + len);
                    break;
                case WireType.LONG_ARRAY:
                    len = bufferReader.readShort(offset) & 0xffff;
                    visitor.onTag(bufferReader, type, offset + 2, len);
                    offset += (SHORT_SIZE + (len * LONG_SIZE));
                    break;
                default:
                    throw new IllegalArgumentException("Unsupported wire type");
            }
        }
    }

}
