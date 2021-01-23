package com.saveteam.ridesharing.business.lsh;

import java.security.*;
import java.math.BigInteger;
import java.util.*;


public class LSH<T> {

	public enum JACCARD_STRATEGY {
		JACCARD_MIN,
		JACCARD_MAX,
		JACCARD_AVERAGE,
		JACCARD
	}

	private final int NumOfHashes = 10000;
	private final int HashValues[][];
	private List<int[]> MinHashingMatrix = new ArrayList<int[]>();
	private int HashMod = 10000;//shingle hashing range: all possible shingles are hashed in range: 0 to HashMod
	private int shingle_length = 5;
	private Map<Integer, boolean[]> Characteristic_Matrix = new HashMap<Integer,boolean[]>();
	private int BandSize = 5;
	private List<Map<String, List<Integer>>> LSHMapping= new ArrayList<Map<String, List<Integer>>>();


	private final int INF = 9999999;
	private static int NumSets;


	public LSH(List<Set<T>> Sets)
	{

		NumSets = Sets.size();	
		HashValues = assignHashValues();


	}
	public Integer HashShingle(String shingle)//to hash shingles to a smaller bucket
	{
		int val = 0;
		for(int i=0;i<shingle_length;i++)
		{
			val += shingle.charAt(i) * Math.pow(shingle_length, i);
		}
		return (Integer)(val%HashMod);

	}
	private void updateCharateristicMatrix(Set<Integer> s1, int setindex) {


		for(Integer shingle: s1)
		{

			if(Characteristic_Matrix.containsKey(shingle))
			{
				boolean[] temp = new boolean[NumSets];
				boolean[] temp2 = new boolean[NumSets];
				//				System.out.println("length = " + temp.length);
				temp = Characteristic_Matrix.get(shingle);
				for(int i=0;i<temp.length;i++)
				{
					temp2[i] = temp[i];

				}
				temp2[setindex] = true;

				Characteristic_Matrix.put(shingle, temp2);
			}
			else
			{
				boolean[] Contains = new boolean[NumSets];
				setfalse(Contains);				
				Contains[setindex] = true;
				//				System.out.println("__DEBUG:2");
				Characteristic_Matrix.put(shingle, Contains);
			}
		}



	}
	private void initSets(List<Set<Integer>> Sets)
	{
		for(int i=0; i < NumSets ;i++)
		{

			updateCharateristicMatrix(Sets.get(i), i);
		}


	}
	private void setfalse(boolean[] contains) {
		for(int i=0;i<NumSets;i++)
		{
			contains[i] = false;
		}
	}

	/**
	 * Create hash matrix with 2 hash function and 4 shingling
	 * h1	h2
	 * 3	1
	 * 2	3
	 * 1	0
	 * 0	2
	 */
	private int[][] assignHashValues()
	{

		List<Integer> permlist = new ArrayList<Integer>();// list to be permuted to get random ordering
		for(int i=0;i<HashMod;i++)
		{
			permlist.add(i);
		}


		int Hashvalues[][] = new int[HashMod][NumOfHashes];//[shingle set size][number of hashes]
		for(int j=0;j<NumOfHashes;j++)
		{
			Collections.shuffle(permlist);
			for(int i=0;i<HashMod;i++)
			{
				Hashvalues[i][j] = permlist.get(i);
			}

		}

		return Hashvalues;
	}

	private void update_minhashing_matrix(Set<Integer> CurrentSet,int SetIndex)
	{
		int[] InitArray = new int[NumOfHashes];

		for(int i=0;i<NumOfHashes;i++)
		{
			InitArray[i] = INF;
			//			MinHashingMatrix[SetIndex][i] = INF;//initial value s
		}
		MinHashingMatrix.add(InitArray);

		for(Integer shingle:Characteristic_Matrix.keySet() )
		{
			if(CurrentSet.contains(shingle))
			{
				for(int i=0;i<NumOfHashes;i++)
				{

					MinHashingMatrix.get(SetIndex)[i] = Math.min(MinHashingMatrix.get(SetIndex)[i], HashValues[shingle][i]);//position of shingle in ith hashing
				}
			}
		}
	}

	private void initMinhashingMatrix(List<Set<Integer>> Sets)
	{
		for(int i=0;i<NumSets;i++)
		{
			update_minhashing_matrix(Sets.get(i),i);
		}
	}


	public Set<Integer> Create_Shingles_optimized(String s)
	{
		Set<Integer> shingle_set = new HashSet<Integer>();
		String shingle;
		Integer HashedShingle;
		shingle = s.substring(0, shingle_length);
		HashedShingle = HashShingle(shingle);
		shingle_set.add(HashedShingle);
		String tempstring;

		for(int i=shingle_length;i<s.length();i++)
		{
			tempstring = shingle.substring(1);
			tempstring += s.charAt(i);
			shingle = tempstring;
			HashedShingle = HashShingle(shingle);
			shingle_set.add(HashedShingle);

		}
		return shingle_set;


	}

	public double findJaccardValue(int SetIndex1, int SetIndex2) {
		double JaccardCoeff;
		int intersectionSize = 0;
		int unionSize = NumOfHashes;

		for(int i=0;i<NumOfHashes;i++)
		{

			if(MinHashingMatrix.get(SetIndex1)[i]==MinHashingMatrix.get(SetIndex2)[i])
			{
				intersectionSize++;
			}

		}
		JaccardCoeff = (intersectionSize*1.0)/unionSize;

		/**
		 * min
		 */
		double min = Math.min(MinHashingMatrix.get(SetIndex1).length, MinHashingMatrix.get(SetIndex2).length);
		JaccardCoeff = (JaccardCoeff * unionSize) / min;


		return JaccardCoeff;

	}

	public double findJaccardValueWithMin(int SetIndex1, int SetIndex2) {
		double jaccardValueForMin = findJaccardValue(SetIndex1, SetIndex2);
		int unionSize = NumOfHashes;
		int min = Math.min(MinHashingMatrix.get(SetIndex1).length, MinHashingMatrix.get(SetIndex2).length);

		jaccardValueForMin = jaccardValueForMin * unionSize / min;
		return jaccardValueForMin;
	}

	public double findJaccardValueWithMax(int SetIndex1, int SetIndex2) {
		double jaccardValueForMax = findJaccardValue(SetIndex1, SetIndex2);
		int unionSize = NumOfHashes;
		int max = Math.max(MinHashingMatrix.get(SetIndex1).length, MinHashingMatrix.get(SetIndex2).length);

		jaccardValueForMax = jaccardValueForMax * unionSize / max;
		return jaccardValueForMax;
	}

	public double findJaccardValueWithAverage(int SetIndex1, int SetIndex2) {
		double jaccardValueForAverage = findJaccardValue(SetIndex1, SetIndex2);
		int unionSize = NumOfHashes;
		int average = (MinHashingMatrix.get(SetIndex1).length + MinHashingMatrix.get(SetIndex2).length) / 2;

		jaccardValueForAverage = jaccardValueForAverage * unionSize / average;
		return jaccardValueForAverage;
	}

	private double compareBetween(int SetIndex1, int SetIndex2)
	{
		double JaccardCoeff = 0;

		JaccardCoeff = findJaccardValueWithMin(SetIndex1, SetIndex2);

		return JaccardCoeff;
	}

	public double getPercent(int SetIndex1, int SetIndex2) {
		double JaccardCoeff;
		int intersectionSize = 0;
		int unionSize = NumOfHashes;

		for(int i=0;i<NumOfHashes;i++)
		{

			if(MinHashingMatrix.get(SetIndex1)[i]==MinHashingMatrix.get(SetIndex2)[i])
			{
				intersectionSize++;
			}

		}
		JaccardCoeff = (intersectionSize*1.0)/unionSize;

		return JaccardCoeff;

	}

	public void getSimilarity(List<Set<Integer>> Sets) {
		int len = NumSets;
		initSets(Sets);
		initMinhashingMatrix(Sets);

		for(int i=0;i<len;i++)
		{
			for(int j=i+1;j<len;j++)
			{
				System.out.println("Document " + i + " and " + j + " are " + findJaccardValue(i,j)*100 + "% similar" );
			}
		}
	}

	public void NewQuery(Set<Integer> newSet) {
		int index = NumSets;
		NumSets++;
		updateCharateristicMatrix(newSet, index);	
		update_minhashing_matrix(newSet,index);
		for(int i=0;i<NumSets;i++)
		{
			for(int j=i+1;j<NumSets;j++)
			{
				System.out.println("Document " + i + " and " + j + " are " + findJaccardValue(i,j)*100 + "% similar" );
			}
		}

	}

	public void PrintSimilarSets() {
		System.out.println("Printing only buckets with size > 1");
		System.out.println("------------------------------------------");
		for(int i=0;i<LSHMapping.size();i++)
		{
			for(String md5Key: LSHMapping.get(i).keySet())
			{
				List<Integer> DocList = LSHMapping.get(i).get(md5Key);

				if(DocList.size()>1)
				{
					System.out.print("Band number " + i + " :");
					for(Integer DocIndex : DocList)
					{
						System.out.print(DocIndex + " ");

					}
					System.out.println();
				}


			}
		}

	}

	public List<Integer> getSimilarOf(int query, double threshold) {
		HashSet<Integer> r = new HashSet<>();
		for(int i=0;i<LSHMapping.size();i++)
		{
			for(String md5Key: LSHMapping.get(i).keySet())
			{
				Set<Integer> DocList = new HashSet<>(LSHMapping.get(i).get(md5Key));

				if(DocList.size()>1)
				{
					if (isContain(new ArrayList<>(DocList), query)) {
						for (Integer index: DocList) {
							if (index != query) {
								double value = compareBetween(query, index);
								if (value > threshold){
									r.add(index);
								}

							}
						}
					}
				}


			}
		}

		return new ArrayList<Integer>(r);
	}

	private static boolean isContain(List<Integer> arr,int value) {
		for (int index : arr) {
			if (index == value) {
				return true;
			}
		}

		return false;
	}

	public Set<Integer> getSimilar() {
		Set<Integer> result = new HashSet<>();
		for(int i=0;i<LSHMapping.size();i++)
		{
			for(String md5Key: LSHMapping.get(i).keySet())
			{
				List<Integer> DocList = LSHMapping.get(i).get(md5Key);

				if(DocList.size()>1)
				{
					for(Integer DocIndex : DocList)
					{
						result.add(DocIndex);
					}
					System.out.println();
				}


			}
		}
		return result;
	}

	public void LSHDriver()// mapping md5 value of band vector to the set number
	{
		int BandIndex = 0;
		for(int j=0;j<NumOfHashes;j+=BandSize)
		{
			LSHMapping.add(new HashMap<String, List<Integer>>());//every band will have different mapping buckets
			Map<String,List<Integer>> Mapping = LSHMapping.get(BandIndex);
			for(int i=0;i<NumSets;i++)
			{

				String HashKey = CreateStringFromIndex(i,j);
				String md5key = getMd5(HashKey);
				if(Mapping.containsKey(md5key))
				{
					List<Integer> tempList = new ArrayList<Integer>(); 
					tempList = Mapping.get(md5key);
					tempList.add(i);

					Mapping.put(md5key, tempList);
//					System.out.println("found key: " + md5key  + " in band number : " + BandIndex);
					//						System.out.println("size after : "+ LSHMapping.get(md5key).size());

				}
				else
				{
					List<Integer> tempList = new ArrayList<Integer>(); 
					tempList.add(i);
					Mapping.put(md5key, tempList);
					//											System.out.println("added key: " + md5key);

				}

			}
			BandIndex++;
		}
	}

	public void UpdateLSHMapping()
	{

		int BandIndex = 0;
		int Setindex = NumSets - 1;
		for(int j=0;j<NumOfHashes;j+=BandSize)
		{
			//			LSHMapping.add(new HashMap<String, Set<Integer>>());
			Map<String,List<Integer>> Mapping = LSHMapping.get(BandIndex);



			String HashKey = CreateStringFromIndex(Setindex,j);//index of new set added = NumSets
			String md5key = getMd5(HashKey);
			if(Mapping.containsKey(md5key))//found candidate matching documents
			{
				List<Integer> tempList = new ArrayList<Integer>(); 
				tempList = Mapping.get(md5key);
				SimilarCandidates(tempList);
				tempList.add(Setindex);

				Mapping.put(md5key, tempList);
				System.out.println("found key: " + md5key  + " in band number : " + BandIndex);

				//					System.out.println("size after : "+ LSHMapping.get(md5key).size());

			}
			else
			{
				List<Integer> tempList = new ArrayList<Integer>(); 
				tempList.add(Setindex);
				Mapping.put(md5key, tempList);
				//					System.out.println("added key: " + md5key);

			}

			BandIndex++;
		}

	}

	private void SimilarCandidates(List<Integer> tempList) {
		System.out.println("Similar Candidates");
		System.out.println("----------------------------");
		for(Integer Doc: tempList)
		{
			System.out.print(Doc + " ");
		}
		System.out.println();
	}

	private String getMd5(String hashKey) { // to convert hashkey to md5 value
		try {
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(hashKey.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "invalid";


	}

	private String CreateStringFromIndex(int setindex, int startindex) {
		String res = "";
		for(int i=startindex;i<startindex+BandSize;i++)
		{
			res += ((Integer)MinHashingMatrix.get(setindex)[i]).toString();
		}

		return res;
	}
}
