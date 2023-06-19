package com.tzaranthony.genshingatcha.registries;

import com.tzaranthony.genshingatcha.core.character.Character;
import com.tzaranthony.genshingatcha.core.character.*;

import java.util.HashMap;

public class GGCharacters {
    public static final Character DILUC = new Diluc(1, 60, 300);
    public static final Character FISCHL = new Fischl(2, 100, 300);
    public static final Character ZHONGLI = new Zhongli(3, 200, 500);
    public static final Character QIQI = new Qiqi(4, 100, 300);
    //kokomi
    //kazuha
    //collei

    public static final HashMap<Integer, Character> characterMap = new HashMap<>() {{
        put(0, null);
        put(DILUC.getCharacterID(), DILUC);
        put(FISCHL.getCharacterID(), FISCHL);
        put(ZHONGLI.getCharacterID(), ZHONGLI);
        put(QIQI.getCharacterID(), QIQI);
    }};
}