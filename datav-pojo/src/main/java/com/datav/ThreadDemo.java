//package com.qyk;
//
//public class ThreadDemo {
//    public static void main(String[] args) throws Exception {
//        final String locks = "锁资源";
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                synchronized (locks) {
//                    try {
//                        System.out.println("Thread A start ，get locks");
//                        System.out.println("Thread A sleep 20ms ");
//                        Thread.sleep(20);
//                        System.out.println("Thread A sleep end");
//                        System.out.println("Thread A：locks is wait!");
//                        locks.wait(10);
//                        System.out.println("Thread A is done");
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        Thread.sleep(10);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                System.out.println("Thread B start ，waiting get locks");
//                synchronized (locks) {
//                    try {
//                        System.out.println("Thread B start ，waiting get locks");
//                        System.out.println("Thread B sleep 20ms ");
//                        Thread.sleep(20);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
//}
