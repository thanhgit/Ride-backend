package com.saveteam.ridesharing.business.lsh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MinHash<T> {

	private final int NumOfHashes;
	private final int HashValues[][];
	private Map<T, boolean[]> Characteristic_Matrix;

	private final int INF = 9999999;
	private final Set<T> Set1;
	private final Set<T> Set2;

	public MinHash(Set<T> S1, Set<T> S2)
	{
		/**
		 * init value
		 */
		this.NumOfHashes = 2;
		this.Set1 = S1;
		this.Set2 = S2;

		/**
		 * create matrix with format (Set<T>, boolean[]) EX: ({1,2,3,8,5,4}, [true, false, true, true, true, false])
		 * có thể ví true -> 1 và false -> 0
		 */
		Characteristic_Matrix = createCharateristicMatrix(S1,S2);

		/**
		 * Là một mảng 2 chiều
		 */
		HashValues = assignHashValues(NumOfHashes,S1,S2);

		System.out.println("------------------Signature------------------------");

	}
	private Map<T, boolean[]> createCharateristicMatrix(Set<T> s1, Set<T> s2) {

		Map<T,boolean[]> Char_matrix = new HashMap<T,boolean[]>();
		for(T shingle: s1)// assign true to shingles in set s1 and default false assuming this shingle is not present in set s2
		{
			Char_matrix.put(shingle, new boolean[] {true,false} );
		}
		for(T shingle: s2)
		{
			if(s1.contains(shingle))
			{
				Char_matrix.put(shingle, new boolean[] {true,true} );
			}
			else
			{
				Char_matrix.put(shingle, new boolean[] {false,true} );
			}
		}

		System.out.println("Resuling of create characteristic matrix");
		for (T key: Char_matrix.keySet()) {
			String str = (String) key;
			System.out.println("key: "+ key + " value: " + ((boolean[])Char_matrix.get(key))[0] + " " + ((boolean[])Char_matrix.get(key))[1]);
		}

		return Char_matrix;
	}
	private int[][] assignHashValues(int NumHashes,Set<T> S1,Set<T> S2)
	{

		/**
		 * Lấy tập giao của 2 tập
		 */
		Set<T> Union = new HashSet<T>(S1);
		Union.addAll(S2);//union of both sets to create hashvalues matrix
		/**
		 * Lấy số lượng tập giao
		 */
		int Union_size = Union.size();
		List<Integer> permlist = new ArrayList<Integer>();
		for(int i=0;i<Union_size;i++)
		{
			permlist.add(i);
		}

		/**
		 * Matrix ()
		 */
		int Hashvalues[][] = new int[Union_size][NumHashes];
		for(int j=0;j<NumHashes;j++)
		{
			/**
			 * hàm trộn
			 */
			Collections.shuffle(permlist);
			for(int i=0;i<Union_size;i++)
			{
				System.out.println("index ("+i+", "+j+") =" + permlist.get(i));
				Hashvalues[i][j] = permlist.get(i);
			}
			
		}

		return Hashvalues;
	}

	private int[] create_minhashing_matrix(Set<T> CurrentSet)
	{
//		System.out.println("---------------------------");
		int Minhashing_matrix[] = new int[NumOfHashes];
		for(int i=0;i<NumOfHashes;i++)
		{
			Minhashing_matrix[i] = INF;//initial value s
		}

		int shingleindex = 0;
		for(T shingle:Characteristic_Matrix.keySet() )
		{
			System.out.println("__DEBUG: shingle =  " + shingle);
			for(int i=0;i<NumOfHashes;i++)
			{

				if(CurrentSet.contains(shingle))
				{
					System.out.println("__DEBUG : " + HashValues[shingleindex][i]);
					Minhashing_matrix[i] = Math.min(Minhashing_matrix[i], HashValues[shingleindex][i]);//position of shingle in ith hashing
				}
			}
			shingleindex++;

		}


		return Minhashing_matrix;
	}

	/**
	 * Tìm sự tương dồng của 2 tập hợp qua thước đo jaccard
	 * @return giá trị thể hiện sự tương đồng
	 */
	private double findJaccardValue()
	{
		double JaccardCoeff;
		/**
		 * Số lượng item giao nhau của 2 tập hợp
		 */
		int intersectionSize = 0;
		int unionSize = NumOfHashes;
		/**
		 * matrix minhash của tập 1
		 */
		int minHashingMatrix1[] = create_minhashing_matrix(Set1);
		/**
		 * matrix minhash của tập 2
		 */
		int minhashingMatrix2[] = create_minhashing_matrix(Set2);

		/**
		 * Tìm tập giao của 2 tập hợp
		 */
		for(int i=0;i<NumOfHashes;i++)
		{
			System.out.println("__DEBUG " + minHashingMatrix1[i] +  " "  + minhashingMatrix2[i]);
		
			if(minHashingMatrix1[i]==minhashingMatrix2[i])
			{
				intersectionSize++;
			}

		}

		/**
		 * 1.0 to catch int to float
		 */
		JaccardCoeff = (intersectionSize*1.0)/unionSize;

		return JaccardCoeff;

	}

	public static void main(String[] args) {

		Set<String> setA = new HashSet<String>();
		setA.add("shingle1");
		setA.add("shingle2");
		setA.add("shingle3");
		setA.add("shingle4");
		setA.add("shingle7");
		Set<String> setB = new HashSet<String>();
		setB.add("shingle1");
		setB.add("shingle7");
		setB.add("shingle35");
		setB.add("shingle13");
		
		MinHash<String> obj = new MinHash<String>(setA, setB);
		System.out.println("Similarity = " + obj.findJaccardValue());
		
		

	}





}