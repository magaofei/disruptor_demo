package com.github.magaofei;

import com.lmax.disruptor.RingBuffer;

import java.nio.ByteBuffer;

public class LongEventProducerLegacy {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducerLegacy(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void onData(ByteBuffer bb) {
        long sequence = ringBuffer.next();
        try {
            LongEvent event = ringBuffer.get(sequence);
            event.set(bb.getLong(0));
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
