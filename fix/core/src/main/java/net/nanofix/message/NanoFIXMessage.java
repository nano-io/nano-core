package net.nanofix.message;

import io.nano.core.buffer.ByteBufferUtil;
import net.nanofix.message.util.ChecksumCalculator;
import net.nanofix.util.ByteArrayUtil;

import java.nio.ByteBuffer;

/**
 * This class has mutable state so concurrent use is not advised.
 */
public class NanoFIXMessage extends FixBuffer implements FIXMessage {

    private final MessageHeader header;

    public NanoFIXMessage(ByteBuffer messageBuffer) {
        super(messageBuffer);
        this.header = new MessageHeader();
    }

    @Override
    public MessageHeader header() {
        return header;
    }

    @Override
    public MsgType msgType() {
        return header.msgType();
    }

    @Override
    public long encode(final ByteBuffer buffer, final int offset) {
        int index = offset;
        int bodyLength = ByteBufferUtil.readableBytes(buffer);
        header.encode(buffer, index, bodyLength);

        // TODO offset / length ??
        buffer.put(buffer());

        // update the checksum
        int length = buffer.limit();
        int checksum = ChecksumCalculator.calculateChecksum(buffer, offset, length);
        addBytesField(Tags.CheckSum, ByteArrayUtil.as3ByteArray(checksum));
        return -1;
    }

}