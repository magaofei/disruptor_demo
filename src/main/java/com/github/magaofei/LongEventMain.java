package com.github.magaofei;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

public class LongEventMain {

    public static void handleEvent(LongEvent event, long sequence, boolean endOfBatch) {
        System.out.println("handle Event " + event);
    }

    public static void translate(LongEvent event, long sequence, ByteBuffer buffer) {
        event.set(buffer.getLong(0));
    }


    public static void main(String[] args) throws InterruptedException {

        LongEventFactory factory = new LongEventFactory();
        // 必须是 2 的次方
        int bufferSize = 1024;
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(
            factory, bufferSize, DaemonThreadFactory.INSTANCE,
            ProducerType.SINGLE,
            new BlockingWaitStrategy());

        disruptor.handleEventsWith((longEvent, l, b) -> System.out.println("Event: " + longEvent));
        disruptor.start();

        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
        LongEventProducer producer = new LongEventProducer(ringBuffer);
        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long i = 0; true; i++) {
            bb.putLong(0, i);
            producer.onData(bb);
//            ringBuffer.publishEvent(LongEventMain::translate, bb);
            Thread.sleep(1000);
        }
    }
}
