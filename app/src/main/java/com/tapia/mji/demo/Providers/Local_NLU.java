package com.tapia.mji.demo.Providers;

import android.content.Context;

import com.tapia.mji.demo.Actions.MyAction;
import com.tapia.mji.demo.Actions.Rotate;
import com.tapia.mji.demo.Languages.Japanese;
import com.tapia.mji.demo.R;
import com.tapia.mji.tapialib.Actions.Action;
import com.tapia.mji.tapialib.Languages.Language;
import com.tapia.mji.tapialib.Providers.Interfaces.OfflineNLUProvider;
import com.tapia.mji.tapialib.Utils.LevenshteinDistance;
import com.tapia.mji.tapialib.Utils.TapiaRobot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.tapia.mji.demo.Actions.MyAction.MyActionType.GIVE_TIME;

/**
 * Created by Sami on 07-Jul-16.
 */
public class Local_NLU implements OfflineNLUProvider {

    Language.LanguageID language;

    ArrayList<Keyword> myKeywords = new ArrayList<>();
    OnAnalyseCompleteListener onAnalyseCompleteListener;
    static Local_NLU myInstance = null;
    static public Local_NLU getInstance(Context context, Language.LanguageID language){
        if(myInstance == null || !language.equals(myInstance.language)){
            myInstance = new Local_NLU(context,language);
        }
        return myInstance;
    }

    class Keyword{
        String[] keywordArray;
        Action.ActionType actionType;
        Keyword (String[] keywordArray, Action.ActionType actionType){
            this.keywordArray = keywordArray;
            this.actionType = actionType;
        }
    }
    Context context;

    public Local_NLU(Context context, Language.LanguageID language){
        this.language = language;
        this.context = context;
//        myKeywords.add(new Keyword(new String[]{"call","record"}, Action.ActionType.));
//        myKeywords.add(new Keyword(new String[]{"keyword", "setting"}, Action.ActionType.));
        if(language == Language.LanguageID.ENGLISH_US || language == Language.LanguageID.ENGLISH_UK) {
            myKeywords.add(new Keyword(new String[]{"what", "time"}, GIVE_TIME));
            myKeywords.add(new Keyword(new String[]{"rotate", "degree"}, MyAction.MyActionType.ROTATE));


        }
        else if(language == Language.LanguageID.JAPANESE){
            myKeywords.add(new Keyword(new String[]{"何時"}, MyAction.MyActionType.GIVE_TIME));
            myKeywords.add(new Keyword(new String[]{"なんじ"}, MyAction.MyActionType.GIVE_TIME));
            myKeywords.add(new Keyword(new String[]{"何曜日"}, MyAction.MyActionType.WEEK));
            myKeywords.add(new Keyword(new String[]{"なんようび"}, MyAction.MyActionType.WEEK));
            myKeywords.add(new Keyword(new String[]{"何日"}, MyAction.MyActionType.GIVE_DATE));
            myKeywords.add(new Keyword(new String[]{"なんにち"}, MyAction.MyActionType.GIVE_DATE));
            myKeywords.add(new Keyword(new String[]{"回転", "度"}, MyAction.MyActionType.ROTATE));
            myKeywords.add(new Keyword(new String[]{"かんだ"},MyAction.MyActionType.KANDA));
            myKeywords.add(new Keyword(new String[]{"天気"},MyAction.MyActionType.WEATHER));
            myKeywords.add(new Keyword(new String[]{"てんき"},MyAction.MyActionType.WEATHER));
            myKeywords.add(new Keyword(new String[]{"移動"},MyAction.MyActionType.MOVE));
            myKeywords.add(new Keyword(new String[]{"いどう"},MyAction.MyActionType.MOVE));
            myKeywords.add(new Keyword(new String[]{"ニュース"},MyAction.MyActionType.NEWS));
            myKeywords.add(new Keyword(new String[]{"カメラ"},MyAction.MyActionType.CAMERA));
            myKeywords.add(new Keyword(new String[]{"はじめまして"},MyAction.MyActionType.NICETOMEETYOU));
            myKeywords.add(new Keyword(new String[]{"おはよう"},MyAction.MyActionType.GOODMORNING));
            myKeywords.add(new Keyword(new String[]{"こんにちは"},MyAction.MyActionType.HELLO));
            myKeywords.add(new Keyword(new String[]{"こんばんは"},MyAction.MyActionType.GOODEVENING));
            myKeywords.add(new Keyword(new String[]{"ただいま"},MyAction.MyActionType.IMHOME));
            myKeywords.add(new Keyword(new String[]{"おやすみ"},MyAction.MyActionType.GOODNIGHT));
            myKeywords.add(new Keyword(new String[]{"サポート"},MyAction.MyActionType.SUPPORT));
            myKeywords.add(new Keyword(new String[]{"ヘルプ"},MyAction.MyActionType.SUPPORT));
            myKeywords.add(new Keyword(new String[]{"だいいちじぎょうぶ"},MyAction.MyActionType.DIVISION1));
            myKeywords.add(new Keyword(new String[]{"だいにじぎょうぶ"},MyAction.MyActionType.DIVISION2));
            myKeywords.add(new Keyword(new String[]{"だいさんじぎょうぶ"},MyAction.MyActionType.DIVISION3));
            myKeywords.add(new Keyword(new String[]{"ぎじゅつほんぶ"},MyAction.MyActionType.DIVISIONTECH));
            myKeywords.add(new Keyword(new String[]{"きばん"},MyAction.MyActionType.KIBAN));
            myKeywords.add(new Keyword(new String[]{"とうかつ"},MyAction.MyActionType.TOKATU));
            myKeywords.add(new Keyword(new String[]{"プロジェクト"},MyAction.MyActionType.TOKATU));
            myKeywords.add(new Keyword(new String[]{"ICT"},MyAction.MyActionType.ICT));
            myKeywords.add(new Keyword(new String[]{"だいいち","えいぎょう"},MyAction.MyActionType.EIGYO1));
            myKeywords.add(new Keyword(new String[]{"だいに","えいぎょう"},MyAction.MyActionType.EIGYO2));
            myKeywords.add(new Keyword(new String[]{"だいさん","えいぎょう"},MyAction.MyActionType.EIGYO3));
            myKeywords.add(new Keyword(new String[]{"だいいち","うんよう"},MyAction.MyActionType.UNYO1));
            myKeywords.add(new Keyword(new String[]{"だいに","うんよう"},MyAction.MyActionType.UNYO2));
            myKeywords.add(new Keyword(new String[]{"だいさん","うんよう"},MyAction.MyActionType.UNYO3));
            myKeywords.add(new Keyword(new String[]{"だいいち","かいはつ"},MyAction.MyActionType.KAIHATU1));
            myKeywords.add(new Keyword(new String[]{"だいに","かいはつ"},MyAction.MyActionType.KAIHATU2));
            myKeywords.add(new Keyword(new String[]{"だいさん","かいはつ"},MyAction.MyActionType.KAIHATU3));
            myKeywords.add(new Keyword(new String[]{"かんりぶ"},MyAction.MyActionType.KANRI));
        }
    }

    @Override
    public void analyseText(final List<String> sentences, final List<Action> actionToListen) {
        new Runnable() {
            @Override
            public void run() {

                ArrayList<Keyword> listToAnalyse = new ArrayList<Keyword>();
                for (Action act: actionToListen) {
                    for (Keyword keyword: myKeywords) {
                        if(keyword.actionType == act.getType()){
                            listToAnalyse.add(keyword);
                        }
                    }
                }
                Keyword keywordGroup = getSimpleBestKeywordsGroup(listToAnalyse,sentences);
                Action resultAction = null;
                if(keywordGroup != null){
                    switch ((MyAction.MyActionType)keywordGroup.actionType) {
                        case ROTATE:
                            Rotate rotate = (Rotate) Action.queryAction(actionToListen, MyAction.MyActionType.ROTATE);
                            if(rotate != null) {
                                for (String sentence : sentences) {
                                    if (sentence.contains(context.getString(R.string.direction_left0)))
                                        rotate.setOrientation(TapiaRobot.RotateOrientation.LEFT);
                                    else if(sentence.contains(context.getString(R.string.direction_right0)))
                                        rotate.setOrientation(TapiaRobot.RotateOrientation.RIGHT);
                                    else if(sentence.contains(context.getString(R.string.direction_up0)))
                                        rotate.setOrientation(TapiaRobot.RotateOrientation.UP);
                                    else if(sentence.contains(context.getString(R.string.direction_down0)))
                                        rotate.setOrientation(TapiaRobot.RotateOrientation.DOWN);
                                }
                                int degrees = -1;
                                for (String sentence : sentences) {
                                    try {
                                        if(language.equals(Language.LanguageID.JAPANESE)){
                                            sentence = Japanese.convertFullWidthNumberToHalfWidthNumber(sentence);
                                        }

                                        degrees = Integer.parseInt(sentence.replaceAll("[\\D]", ""));
                                        rotate.setDegree(degrees);
                                        break;
                                    } catch (Exception e) {
                                        degrees = -1;
                                    }
                                }
                                if (degrees != -1)
                                    resultAction = rotate;
                                else
                                    resultAction = null;
                            }
                            break;
                        default:
                            resultAction = Action.queryAction(actionToListen,keywordGroup.actionType);
                            break;
                    }
                }
                if(resultAction != null) {
                    resultAction.onAction();
                }
                if(onAnalyseCompleteListener !=null) {
                    onAnalyseCompleteListener.OnAnalyseComplete(resultAction);
                }
            }
        }.run();
    }

    @Override
    public void setOnAnalyseCompleteListener(OnAnalyseCompleteListener onAnalyseCompleteListener) {
        this.onAnalyseCompleteListener = onAnalyseCompleteListener;
    }

    public Keyword getSimpleBestKeywordsGroup(ArrayList<Keyword> keywords, List<String> sentences){
        Keyword bestKeywords = null;
        for (int k = sentences.size() - 1; k >= 0; k--) {
            for (Keyword kw: keywords) {
                boolean isMatch = true;
                for (String word: kw.keywordArray) {
                    if(!sentences.get(k).toLowerCase().contains(word.toLowerCase()))
                        isMatch = false;
                }
                if(isMatch)
                    bestKeywords = kw;
            }
        }

        return bestKeywords;
    }

    public String[] getBestKeywordsGroup(ArrayList<String[]> keywords, List<String> sentences){
        int[] weights = new int[keywords.size()];

        for (int k = 0; k < keywords.size(); k++) {
            String[] keyword =  keywords.get(k);
            for (int l =  0; l < keyword.length; l++) {
                String keywordOnlyWords = keyword[l].replace(",", "").replace(".", "");
                List<String> keywordWords = Arrays.asList(keywordOnlyWords.split(" "));
                for(int i = 0; i < sentences.size(); i++){
                    String resultOnlyWords = sentences.get(i).replace(",", "").replace(".", "");
                    List<String> resultWords = Arrays.asList(resultOnlyWords.split(" "));
                    if(keywordOnlyWords.length() < 8 && keywordWords.size() > 1) {
                        if (resultOnlyWords.contains(keywordOnlyWords))
                            weights[k] += (sentences.size() + 1 - i)*keywordWords.size();
                    }
                    else {
                        for (String resultWord : resultWords) {
                            for (String keywordWord : keywordWords) {
                                if (keywordWord.length() <= 4 && keywordWord.length() > 1)
                                    if (LevenshteinDistance.computeLevenshteinDistance(keywordWord.toLowerCase(), resultWord.toLowerCase()) == 0) {
                                        weights[k] += sentences.size() + 1 - i;
                                    }
                                if (keywordWord.length() > 4 && keywordWord.length() < 8) {
                                    if (LevenshteinDistance.computeLevenshteinDistance(keywordWord.toLowerCase(), resultWord.toLowerCase()) <= 1)
                                        weights[k] += sentences.size() + 2 - i - LevenshteinDistance.computeLevenshteinDistance(keywordWord.toLowerCase(), resultWord.toLowerCase());
                                }
                                if (keywordWord.length() >= 8) {
                                    if (LevenshteinDistance.computeLevenshteinDistance(keywordWord.toLowerCase(), resultWord.toLowerCase()) <= 2)
                                        weights[k] += sentences.size() + 3 - i - LevenshteinDistance.computeLevenshteinDistance(keywordWord.toLowerCase(), resultWord.toLowerCase());
                                }
                            }
                        }
                    }
                }
                if(l==0 && weights[k] == 0)
                    break;
            }
        }
        int bestWeightPos = 0;
        int bestWeightValue =0;
        for (int j = 0; j < weights.length;j++) {
            if(bestWeightValue < weights[j]){
                bestWeightValue = weights[j];
                bestWeightPos = j;
            }
        }
        //no keyword found
        if(bestWeightValue <= 20)
            return null;
        else {
            int drawNumber = 0;
            for (int weight : weights) {
                if(weight == weights[bestWeightPos])
                    drawNumber++;
            }
            if(drawNumber > 1)
                return null;

            return keywords.get(bestWeightPos);
        }
    }
}
