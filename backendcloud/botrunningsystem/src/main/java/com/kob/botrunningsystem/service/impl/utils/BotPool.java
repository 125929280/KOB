package com.kob.botrunningsystem.service.impl.utils;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class BotPool extends Thread {
//    private final ReentrantLock lock = new ReentrantLock();
//    private final Condition condition = lock.newCondition();
//    private final Queue<Bot> bots = new LinkedList<>();
    private final ArrayBlockingQueue<Bot> bots = new ArrayBlockingQueue<>(100);

    public void addBot(Integer userId, String botCode, String input, Integer opponentId) {
//        lock.lock();
        try {
//            bots.add(new Bot(userId, botCode, input, opponentId));
            bots.put(new Bot(userId, botCode, input, opponentId));
//            condition.signalAll();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
//            lock.unlock();
        }
    }

    private void consume(Bot bot) {
        Consumer consumer = new Consumer();
        consumer.startTimeout(2000, bot);
    }

    @Override
    public void run() {
        while (true) {
//            lock.lock();
//            if (bots.isEmpty()) {
//                try {
//                    // await方法自动释放锁
//                    condition.await();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                    lock.unlock();
//                    break;
//                }
//            } else {
//                Bot bot = bots.remove();
//                lock.unlock();
//                consume(bot);
//            }
            try {
                Bot bot = bots.take();
                consume(bot);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
