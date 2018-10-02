package net.nanofix.message;

import java.nio.ByteBuffer;

/**
 * User: Mark Wardell
 * Date: 11/10/11
 * Time: 07:52
 */
public interface FIXMessageDecoder2 {

    void decode(ByteBuffer buffer, FIXMessageVisitor2 visitor);

}
