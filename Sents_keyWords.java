/**
Program  Summary:
 
   -> reads senteces line by line from story.txt and stors sentece in ArrayList.
   -> remove all panctuation marks and store words by word in LinkedHashMap
   -> compare words from story.txt with stop words (stop.txt)/ filter out stop words
   -> generate unweighted Graph from Dictionary of story words (LinkedHashMap) with adjacent list of verteces
   -> Rank keywords with frequency of its appearance in the story
 
 **/



import java.io.*;
import java.util.*;
import java.util.Map.Entry;

class Sentence_Rank {
    
    List<String> allSentences = null;
    List<String> allStopWords = null;
    ArrayList<String> words = null;
    List<String> listWords = null;
    
    Map<String, List<Integer>> lmap = new HashMap<String, List<Integer>>();
    
    //lineked-HashMp with a node
    private LinkedHashMap<String, Integer> Dmap;
    public Sentence_Rank() {
        allSentences = new ArrayList<String>();
        allStopWords = new ArrayList<String>();
        words=new ArrayList<String>();
        listWords=new ArrayList<String>();
        // lined-hashMap
        Dmap = new LinkedHashMap<String, Integer>();
    }
    /**
     * readStoryDouments(), designed to read sentence using a buffer reader
     * from .txt file and store it in ArrayList
     */
    public void readStoryDocument() {
        
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("story.txt"), 10240);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String line = "";
        try {
            int val=0;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                String[] sentences = line.split("\\#");
                for (int i = 0; i < sentences.length; i++) {
                    allSentences.add(sentences[i]);
                    String wordsToRead = processWord(sentences[i]);
                    // Break_sentence to words
                    String[] tokens = wordsToRead.split(" ");  //
                    
                    for (int j = 0; j < tokens.length; j++){
                        //check a word for to stop words
                        boolean sam= chechForStopWords(tokens[j]);
                        if(sam==false){
                            // add words for count
                            listWords.add(tokens[j]);
                            // add words in linkedHashMap Dictionary
                            Dmap.put(tokens[j], val );
                        }
                    }
                    
                }//
                ++val;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    
    }// End of readStroyDocument
    
    /**
     * readStopWords(), designed to read words using a buffer reader
     * from .txt file and store it in ArrayList.
     */
    public void readStopWords() {
        
        BufferedReader swd = null;
        try {
            swd = new BufferedReader(new FileReader("stop.txt"), 10240);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String word = "";
        try {
            while ((word = swd.readLine()) != null) {
                word = word.trim();
                String[] stopWords = word.split("\\#");
                for (int i = 0; i < stopWords.length; i++) {
                    allStopWords.add(stopWords[i]);
                }
                
            }
            swd.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    
    }// end of stop
    
    
    /**
     * processWord, function to to change all lowercase
     * remove all Pancturations / change it tolowercase()
     */
    
    private static String processWord(String x) {
        String lower = x.toLowerCase();
        return lower.replaceAll("[\\Q][(){}'-,.;!?<>%\\E]", "");
    }
    
    
    /**
     * checkForStopWords, function to check the story words if it's also stop word
     * if found stop word remove it.
     */
    
    private  boolean chechForStopWords(String x) {
        String stWord = x.toLowerCase();
        boolean sam=false;
        for (int i = 0; i < allStopWords.size(); i++) {
            
            if(stWord.compareTo(allStopWords.get(i))==0){
                sam=true;
            }
            
        }
        
        return sam;
    }
    
    /**
     * printLinedHashMap(), function to prit linkedHashMap
     * using iteration/set
     */
    public void printLinkedHashMap(){
        System.out.println("Undirected Adjacent Vertices");
        System.out.println("****************************");
        Set<Entry<String, Integer>> set = Dmap.entrySet();
        // Displaying elements of LinkedHashMap
        Iterator<Entry<String, Integer>> iterator = set.iterator();
        while(iterator.hasNext()) {
            Entry<String, Integer> me = iterator.next();
            System.out.print( me.getKey() +
                             "  <--> "+ me.getValue()+"\n");
        }
    }
    
    /**
     *underictedGraph(), extract number of Edges(E) and number of Vertices
     * Create undirected/unweighted graph
     */
    public void underictedGraph(){
        
        //add Element to aset to count number of vertices
        Set<Comparable> graphSet = new HashSet<Comparable>();
        // Add some elements to the set
        Set<Entry<String, Integer>> set = Dmap.entrySet();
        // Displaying elements of LinkedHashMap
        Iterator<Entry<String, Integer>> iterator = set.iterator();
        while(iterator.hasNext()) {
            Entry<String, Integer> me = iterator.next();
            graphSet.add( me.getKey());
            graphSet.add(me.getValue());
            //System.out.print( me.getKey() +
            //   "  <--> "+ me.getValue()+"\n");
        }
        int V=graphSet.size();// number of vertices
        int E=Dmap.size(); //number of Edges
        System.out.println();
    
    }
    
    
    /**
     *keyWordsCount(), function count the frequency of words appearance
     * word count to find the top 10 key words out of the story
     */
    public void keyWordsCount(){
        
        Set<String> unique = new HashSet<String>(listWords);
        List<String> uniqueList = new ArrayList<String>(unique);
        Collections.sort(uniqueList, new FrequencyComparator(listWords));
        // print top 10 key words
        System.out.println();
        for(int a=0; a<10; a++){
            System.out.println(uniqueList.get(a) + " ------  " +Collections.frequency(listWords, uniqueList.get(a) ) );
        }
        System.out.println();
        
    }
    /**
     * FrequencyComparator class to calculate the frequency of Words in the story 
     *
     */
    
    public class FrequencyComparator implements Comparator<String>{
        
        List<String> list;
        
        @Override
        public int compare(String o1, String o2) {
            if (Collections.frequency(list, o1) > Collections.frequency(list, o2)){
                return -1;
            }else if (Collections.frequency(list, o1) < Collections.frequency(list, o2)){
                return 1;
            }else{
                return o1.compareTo(o2);
            }
        }
        
        public FrequencyComparator(List<String> list){
            this.list = list;
        }
        
    }
    
    /**
     * Main function  
     * 
     */	 		
    public static void main(String[] args) {
        //inistanciate sentence_rank class 		
        Sentence_Rank D = new Sentence_Rank();
        D.readStopWords();
        D.readStoryDocument();
        D.underictedGraph();
        //D.printLinkedHashMap();	
        D.keyWordsCount();
        
    }// end of main 
    
}// end of Sentence_Rank Class
