package sk.fiit.nemecek.jolana.server.services.stemmer;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import sk.fiit.nemecek.jolana.server.services.database.data.KorpusItem;

/**
 * This is implementation of PreprocessorService.
 * 
 * @see sk.fiit.nemecek.jolana.server.services.stemmer.PreprocessorService
 * @author Tomáš Nemeèek
 * 
 */
@Service
public class ProcessorDefaultService implements ProcessorService {
//
//    @Autowired
//    DatabaseService databaseService;
//    @Autowired
//    ThreadPoolTaskExecutor taskExecutor;
//    
//    private ConcurrentHashMap<Long, String> textMap = new ConcurrentHashMap<Long, String>();
//    
//    @Override
//    public ExtractedDocument processText(ExtractedDocument doc, boolean isStopWord) throws Exception {
//        
//        //get text from Document
//        String a = doc.getText();
//        
//        // tokenize input text
//        List<String> tokenList = new ArrayList<String>();
//        TokenizerFactory tokenizerFactory = new IndoEuropeanTokenizerFactory();
//        Tokenizer tokenizer = tokenizerFactory.tokenizer(a.toCharArray(), 0, a.length());
//        tokenizer.tokenize(tokenList,new ArrayList<String>());
//
//        long counter = 0;
//        StringBuilder sBuilder = new StringBuilder();
//        for (String s : tokenList) {
//            //replace interpunction
//            s = s.replaceAll("\\p{Punct}", "");
//            if (s.length() > 0) {
//                //multithreaded preprocessing of words 
//                while(true){
//                    int count = taskExecutor.getActiveCount();
//                    if(count < taskExecutor.getMaxPoolSize()){
//                        counter++;
//                        taskExecutor.execute(new ProcessTask(s, counter, isStopWord));
//                        break;
//                    }
//                    else {
//                        Thread.sleep(1000);
//                    }
//                }
//            }
//        }
//        
//        //wait for all threads to finish
//        while (taskExecutor.getActiveCount() > 0){
//            Thread.sleep(2000);
//        }
//        //append words in order
//        for(long iterator = 1;iterator <= counter;iterator++){
//            if (textMap.get(iterator).length() > 0) {
//                sBuilder.append(textMap.get(iterator) + " ");
//            }
//        }
//        doc.setText("");
//        doc.setText(sBuilder.toString());
//        return doc;
//    }
//
//    /**
//     * Method for preprocessing of word s at position index. This method return
//     * preprocessed word or the original word.
//     * 
//     * @param String s
//     * @param long index
//     * @return String
//     */
//    private String processWord(String s, long index, boolean isStopWord) {
//        s=s.toLowerCase();
//        if(s.matches("//d+")){
//            return "NUM";
//        }
//        
//        
//        String processingString = new StringBuffer(s).reverse().toString();
//        List<CharacterItem> characters = new ArrayList<CharacterItem>();
//        KorpusItem item = null;
//        // for all chars in the word
//        for (int i = 0; i < processingString.length(); i++) {
//            String character = Character.toString(processingString.charAt(i));
//            if (i == 0) {
//                //if the end char is  return this word (probably "neurcitok")
//                if (character.equals("")) {
//                    return s ;
//                }
//                // search in DB for end char
////                item = databaseService.findSubTreeByLetter(character, true);
//              //if we cannot find end char return as unknown
//                if (item == null) {
//                    return s;
//                }
//            }
//            else {
//                // search for next char
//                KorpusItem temp = null;
//                for (KorpusItem child : item.getChildren().getList()) {
//                    if (child.getPismeno().equals(character)) {
//                        temp = child;
//                        break;
//                    }
//                }
//                item = temp;
//            }
//
//            CharacterItem charItem = null;
//            if (item != null) {
//                //set item into character object
////                item = databaseService.findSubTreeByLetter(item.getId());
//                charItem = new CharacterItem(character, computeEntropy(item));
//            }
//            else {
//                //if we cannot find item in DB create mock object
//                charItem = new CharacterItem(character, 10);
//                item = new KorpusItem();
//            }
//            characters.add(charItem);
//        }
//        for(CharacterItem i :characters){
//            System.out.println(i.getPrecision());
//        }
//        System.out.println();
//        // compute difference entropy
////        characters = recalculateEntropy(characters);
//        //compute base of the word
//        String output = chooseBase(characters);
//        for(CharacterItem i :characters){
//            System.out.println(i.getPrecision());
//        }
//        System.out.println();
//        if (output == null) {
//            output = s;
//        }
//        return output ;
//    }

    /**
     * Method for determining the root of the word. Input of this method is list
     * of words letters with the probability value. It returns the choosen root
     * of the word.
     * 
     * @param List
     *            <CharacterItem>characters
     * @return List<CharacterItem>
     */
    private List<CharacterItem> recalculateEntropy(List<CharacterItem> characters) {
        List<CharacterItem> outList = new ArrayList<CharacterItem>();
        //copy input list for modifications
        for (CharacterItem i : characters) {
            outList.add(i.clone());
        }
        //compute entropy 
        for (int i = 0; i < characters.size()-1; i++) {
            outList.get(i).setPrecision(characters.get(i).getPrecision() - characters.get(i + 1).getPrecision());
        }
        return outList;
    }

    /**
     * Method for choosing the base of the word. It returns the root of the word as String.
     * @param List<CharacterItem> list
     * @return String
     */
    private String chooseBase(List<CharacterItem> list) {
        StringBuilder base = new StringBuilder();
        int j = 0;
        
      for (int i = 0; i < list.size()-1; i++) {
          if(list.get(i).getPrecision() < list.get(i+1).getPrecision()){
              j=i;
              break;
          }
      }
      for(int i = j+1; i < list.size(); i++){
          base.append(list.get(i).getCharacter());
      }
        
        return base.reverse().toString();
    }

    /**
     * Method for computing final entropy of each letter.
     * @param KorpusItem item
     * @return double
     */
    private double computeEntropy(KorpusItem item) {
        double finalPrecision = 0;
        for (KorpusItem ki : item.getChildren().getList()) {
            finalPrecision = finalPrecision + (computeSequence(ki.getProbability() / item.getProbability()));
        }
        return (-1)*finalPrecision;
    }

    /**
     * Method for computing the equation p(s|r)log_2p(s|r).
     * @param probability
     * @return
     */
    private double computeSequence(double probability) {
        return probability * log(probability, 2);
    }

    private double log(double x, int base) {
        return (Math.log(x) / Math.log(base));
    }
    
    /**
     * This class is inner worker class for preprocessing words.
     * @author Tomáš Nemeèek
     *
     */
    class ProcessTask implements Runnable{
        
        String word;
        long counter;
        boolean isStopWord;
     
        public ProcessTask(String name,long counter, boolean isStopWord){
            this.word = name;
            this.counter=counter;
            this.isStopWord=isStopWord;
        }
     
        @Override
        public void run() {
//            String result = processWord(word, counter,isStopWord);
//            synchronized (textMap) {
//                textMap.put(counter, result);
//            }
        }
    }

    @Override
    public String processText(String doc, boolean isStopWord) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
