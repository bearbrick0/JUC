package org.uin.ConcurrentUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * @author wanglufei
 * @description: TODO
 * @date 2022/3/21/9:00 PM
 */
public class ForkJoinDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        System.out.println(forkJoinPool.submit(new SubTask(1, 1000)).get());
    }

    public static class SubTask extends RecursiveTask<Integer> {
        private final int start;   //比如我们要计算一个范围内所有数的和，那么就需要限定一下范围，这里用了两个int存放
        private final int end;

        public SubTask(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        protected Integer compute() {
            if (end - start > 125) {    //每个任务最多计算125个数的和，如果大于继续拆分，小于就可以开始算了
                SubTask subTask1 = new SubTask(start, (end + start) / 2);
                subTask1.fork();    //会继续划分子任务执行
                SubTask subTask2 = new SubTask((end + start) / 2 + 1, end);
                subTask2.fork();   //会继续划分子任务执行
                return subTask1.join() + subTask2.join();   //越玩越有递归那味了
            } else {
                System.out.println(Thread.currentThread().getName() + " 开始计算 " + start + "-" + end + " 的值!");
                int res = 0;
                for (int i = start; i <= end; i++) {
                    res += i;
                }
                return res;   //返回的结果会作为join的结果
            }
        }

    }
}
