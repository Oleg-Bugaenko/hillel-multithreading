public class ValueCalculator {
    private static final int ARRAY_SIZE = 1_000_000;
    private static final int HALF_ARRAY_SIZE = ARRAY_SIZE / 2;
    private static float[] array = new float[ARRAY_SIZE];


    public static void calculateOneThread() {
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < ARRAY_SIZE; i++) array[i] = 1;
        for (int i = 0; i < ARRAY_SIZE; i++) {
            array[i] = (float)(array[i] * Math.sin(0.2f + i/5) * Math.cos(0.2f + i/5) * Math.cos(0.4f + i/2));
        }

        long endTime = System.currentTimeMillis();
        System.out.printf("The task execution time in single-threaded mode was: %d ms \n", endTime-startTime);
    }

    public static void calculateTwoThread() {
        long startTime = System.currentTimeMillis();
        float[] arrayOne = new float[HALF_ARRAY_SIZE];
        float[] arrayTwo = new float[HALF_ARRAY_SIZE];

        for (int i = 0; i < ARRAY_SIZE; i++) array[i] = 1;

        System.arraycopy(array, 0, arrayOne, 0, HALF_ARRAY_SIZE);
        System.arraycopy(array, HALF_ARRAY_SIZE, arrayTwo, 0, HALF_ARRAY_SIZE);

        Thread threadOne = new Thread(() -> {
            for (int i = 0; i < HALF_ARRAY_SIZE; i++) {
                arrayOne[i] = (float)(arrayOne[i] * Math.sin(0.2f + i/5) * Math.cos(0.2f + i/5) * Math.cos(0.4f + i/2));
            }
        });
        threadOne.start();

        Thread threadTwo = new Thread(() -> {
            for (int i = 0; i < HALF_ARRAY_SIZE; i++) {
                int k = HALF_ARRAY_SIZE + i;
                arrayTwo[i] = (float)(arrayTwo[i] * Math.sin(0.2f + k/5) * Math.cos(0.2f + k/5) * Math.cos(0.4f + k/2));
            }
        });
        threadTwo.start();

        try {
            threadOne.join();
            threadTwo.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.arraycopy(arrayOne,0,array,0,HALF_ARRAY_SIZE);
        System.arraycopy(arrayTwo, 0, array, HALF_ARRAY_SIZE, HALF_ARRAY_SIZE);
        long endTime = System.currentTimeMillis();
        System.out.printf("The task execution time in two-threaded mode was: %d ms \n", endTime-startTime);

    }

}
