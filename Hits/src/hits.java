import static java.lang.Math.abs;

import java.io.File;
import java.util.Scanner;

/**
 * This Class implements Hits Algorithm
 * 
 * @author Shubham Agarwal
 *
 */
public class hits {

	static int[][] adjacencyMatrix; // adjacency list
	static int[] outDegree; // Out Degree
	static double[] hubArray;
	static double[] authArray;

	/**
	 * Main method
	 * 
	 * @param args Strings[]
	 */

	public static void main(String[] args) {
		int iterations = 0; // total number of iterations
		int initialValue = 0; // Initial Value
		String fileName = null; // input file

		if (args.length > 2) {
			iterations = Integer.parseInt(args[0]);
			initialValue = Integer.parseInt(args[1]);
			fileName = args[2];
			if ((initialValue <= 1 && initialValue >= -2) == false) {
				System.out.println("Invalid Initial value entered!");
				return;
			}

			hitsAlgo(iterations, initialValue, fileName); // Call Hits algorithm method

		} else {
			System.out.println("Invalid number of arguments entered!");
			return;
		}

	}

	/**
	 * Hub and Auth array initialization
	 * 
	 * @param int vertexCount
	 * @param int initialValue
	 */
	private static void setSrcArray(int vertexCount, int initialValue) {
		int iter = 0;
		hubArray = new double[vertexCount];
		authArray = new double[vertexCount];
		if (initialValue == 0 || initialValue == 1) {
			while (iter < vertexCount) {
				hubArray[iter] = initialValue;
				authArray[iter] = initialValue;
				iter++;
			}
		} else if (initialValue == -1) {
			while (iter < vertexCount) {
				hubArray[iter] = 1.0 / vertexCount;
				authArray[iter] = 1.0 / vertexCount;
				iter++;
			}
		} else if (initialValue == -2) {
			while (iter < vertexCount) {
				hubArray[iter] = 1.0 / Math.sqrt(vertexCount);
				authArray[iter] = 1.0 / Math.sqrt(vertexCount);
				iter++;
			}
		}
	}

	/**
	 * Initialize Authority array
	 * 
	 * @param vertexCount int
	 * @param scaledHub   double[]
	 * @param scaledAuth  double[]
	 * @return double[]
	 */
	private static double[] initializeAuthArray(int vertexCount, double[] scaledHub, double[] scaledAuth) {
		for (int iter = 0; iter < vertexCount; iter++) {
			scaledAuth[iter] = 0.0;
			for (int itr2 = 0; itr2 < vertexCount; itr2++) {
				if (adjacencyMatrix[itr2][iter] == 1) {
					scaledAuth[iter] += scaledHub[itr2];
				}
			}
		}
		return scaledAuth;
	}

	/**
	 * Initialize Hub array
	 * 
	 * @param vertexCount int
	 * @param scaledHub   double[]
	 * @param scaledAuth  double[]
	 * @return double[]
	 */
	private static double[] initializeHubArray(int vertexCount, double[] scaledHub, double[] scaledAuth) {
		for (int iter = 0; iter < vertexCount; iter++) {
			scaledHub[iter] = 0.0;
			for (int itr2 = 0; itr2 < vertexCount; itr2++) {
				if (adjacencyMatrix[iter][itr2] == 1) {
					scaledHub[iter] += scaledAuth[itr2];
				}
			}
		}
		return scaledHub;
	}

	/**
	 * Scale Authority and Hub arrays
	 * 
	 * @param inpArr      double[]
	 * @param vertexCount int
	 * @return double[]
	 */
	private static double[] scaleArray(double inpArr[], int vertexCount) {

		double count = 0.0;
		int iter = 0;
		for (iter = 0; iter < vertexCount; iter++) {
			count += inpArr[iter] * inpArr[iter];
		}
		for (iter = 0; iter < vertexCount; iter++) {
			inpArr[iter] = inpArr[iter] / Math.sqrt(count);
		}

		return inpArr;
	}

	/**
	 * The method to calculate errorate
	 * 
	 * @param srcArr double
	 * @param target double
	 * @return boolean
	 */
	private static boolean errorateCalculation(double[] Arr, double[] targetArr, int vertexCount, int iterations) {
		double errorRate = 0.00001; // Default errorrate of iteration is 0
		if (iterations < 0) {
			errorRate = Math.pow(10, iterations);
		}
		for (int index = 0; index < vertexCount; index++) {
			if (abs(Arr[index] - targetArr[index]) > errorRate) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Hits algorithm
	 * 
	 * @param int    iterations
	 * @param int    initialValue
	 * @param String fileName
	 */
	@SuppressWarnings("resource")
	public static void hitsAlgo(int iterations, int initialValue, String fileName) {
		int vertexCount = 0; // Count of Vertices
		int edgeCount = 0; // Count of Edges
		int noOfRows = 0;
		int noOfCols = 0;
		int itr = 0;
		int itr2 = 0;
		double[] prevAuth = null; // Previous auth values
		double[] prevHub = null; // Previous hub values
		double[] scaledHub = null; // Hub scaled values
		double[] scaledAuth = null; // Auth scaled values
		try {
			Scanner scanner = new Scanner(new File(fileName));
			vertexCount = scanner.nextInt();
			edgeCount = scanner.nextInt();

			adjacencyMatrix = new int[vertexCount][vertexCount];

			while (scanner.hasNextInt()) {
				noOfRows = scanner.nextInt();
				noOfCols = scanner.nextInt();
				adjacencyMatrix[noOfRows][noOfCols] = 1;
			}

			setSrcArray(vertexCount, initialValue);

		} catch (Exception e) {
			System.out.println("Exception: " + e.getMessage());

		}

		prevAuth = new double[vertexCount];
		prevHub = new double[vertexCount];
		scaledHub = new double[vertexCount];
		scaledAuth = new double[vertexCount];

		if (vertexCount > 10) {
			iterations = 0;
			initialValue = -1;
			setSrcArray(vertexCount, initialValue);
			for (itr = 0; itr < vertexCount; itr++) {
				scaledHub[itr] = hubArray[itr];
				scaledAuth[itr] = authArray[itr];
				prevHub[itr] = scaledHub[itr];
				prevAuth[itr] = scaledAuth[itr];
			}

			int i = 0;
			do {
				for (itr = 0; itr < vertexCount; itr++) {
					prevAuth[itr] = scaledAuth[itr];
					prevHub[itr] = scaledHub[itr];
				}
				scaledAuth = initializeAuthArray(vertexCount, scaledHub, scaledAuth);
				scaledHub = initializeHubArray(vertexCount, scaledHub, scaledAuth);
				scaledAuth = scaleArray(scaledAuth, vertexCount);
				scaledHub = scaleArray(scaledHub, vertexCount);
				i++;
			} while (errorateCalculation(scaledAuth, prevAuth, vertexCount, iterations) == false
					|| errorateCalculation(scaledHub, prevHub, vertexCount, iterations) == false);
			System.out.println("Iter   : " + i);
			for (itr = 0; itr < vertexCount; itr++) {
				if (itr < 10)
					System.out.printf("A/H[ %d]=%.7f/%.7f\n", itr,
							Math.round(scaledAuth[itr] * 10000000.0) / 10000000.0,
							Math.round(scaledHub[itr] * 10000000.0) / 10000000.0);
				else
					System.out.printf("A/H[%d]=%.7f/%.7f\n", itr, Math.round(scaledAuth[itr] * 10000000.0) / 10000000.0,
							Math.round(scaledHub[itr] * 10000000.0) / 10000000.0);
			}
			return;
		}

		for (itr = 0; itr < vertexCount; itr++) {
			scaledHub[itr] = hubArray[itr];
			scaledAuth[itr] = authArray[itr];
			prevHub[itr] = scaledHub[itr];
			prevAuth[itr] = scaledAuth[itr];
		}
		System.out.print("Base	:     0 :");
		for (itr = 0; itr < vertexCount; itr++) {
			if (itr == 0)
				System.out.printf("A/H[%d]=%.7f/%.7f", itr, Math.round(authArray[itr] * 10000000.0) / 10000000.0,
						Math.round(hubArray[itr] * 10000000.0) / 10000000.0);
			else
				System.out.printf(" A/H[%d]=%.7f/%.7f", itr, Math.round(authArray[itr] * 10000000.0) / 10000000.0,
						Math.round(hubArray[itr] * 10000000.0) / 10000000.0);
		}
		if (iterations > 0) {

			for (itr = 0; itr < iterations; itr++) {
				scaledAuth = initializeAuthArray(vertexCount, scaledHub, scaledAuth);
				scaledHub = initializeHubArray(vertexCount, scaledHub, scaledAuth);
				scaledAuth = scaleArray(scaledAuth, vertexCount);
				scaledHub = scaleArray(scaledHub, vertexCount);

				System.out.println();
				if (itr < 9)
					System.out.print("Iter	:     " + (itr + 1) + " :");
				else
					System.out.print("Iter	:    " + (itr + 1) + " :");
				for (itr2 = 0; itr2 < vertexCount; itr2++) {
					if (itr2 == 0)
						System.out.printf("A/H[%d]=%.7f/%.7f", itr2,
								Math.round(scaledAuth[itr2] * 10000000.0) / 10000000.0,
								Math.round(scaledHub[itr2] * 10000000.0) / 10000000.0);
					else
						System.out.printf(" A/H[%d]=%.7f/%.7f", itr2,
								Math.round(scaledAuth[itr2] * 10000000.0) / 10000000.0,
								Math.round(scaledHub[itr2] * 10000000.0) / 10000000.0);
				}

			}
		} else {
			int i = 0;
			do {
				for (itr = 0; itr < vertexCount; itr++) {
					prevAuth[itr] = scaledAuth[itr];
					prevHub[itr] = scaledHub[itr];
				}

				scaledAuth = initializeAuthArray(vertexCount, scaledHub, scaledAuth);
				scaledHub = initializeHubArray(vertexCount, scaledHub, scaledAuth);
				scaledAuth = scaleArray(scaledAuth, vertexCount);
				scaledHub = scaleArray(scaledHub, vertexCount);

				i++;
				System.out.println();
				if (i < 10)
					System.out.print("Iter	:     " + i + " :");
				else
					System.out.print("Iter	:    " + i + " :");
				for (itr = 0; itr < vertexCount; itr++) {
					if (itr == 0)
						System.out.printf("A/H[%d]=%.7f/%.7f", itr,
								Math.round(scaledAuth[itr] * 10000000.0) / 10000000.0,
								Math.round(scaledHub[itr] * 10000000.0) / 10000000.0);
					else
						System.out.printf(" A/H[%d]=%.7f/%.7f", itr,
								Math.round(scaledAuth[itr] * 10000000.0) / 10000000.0,
								Math.round(scaledHub[itr] * 10000000.0) / 10000000.0);
				}
			} while (errorateCalculation(scaledAuth, prevAuth, vertexCount, iterations) == false
					|| errorateCalculation(scaledHub, prevHub, vertexCount, iterations) == false);
		}
		System.out.println("\n");
	}
}