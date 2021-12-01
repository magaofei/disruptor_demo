package com.github.magaofei;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    private static final EventTranslatorOneArg<LongEvent, ByteBuffer> TRANSLATOR =
        new EventTranslatorOneArg<LongEvent, ByteBuffer>() {
            @Override public void translateTo(LongEvent longEvent, long l, ByteBuffer byteBuffer) {
                longEvent.set(byteBuffer.getLong(0));
            }
        };

    public void onData(ByteBuffer bb) {
        ringBuffer.publishEvent(TRANSLATOR, bb);
    }


}
