package com.tzaranthony.genshingatcha.core.entities.mobs;

import com.tzaranthony.genshingatcha.core.util.Element;
import com.tzaranthony.genshingatcha.core.util.tags.GGBiomeTags;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.level.biome.Biome;

import java.util.Random;

public interface ElementalEntity {
    void setElement(int elementId);

    int getElement();

    default int getElementFromBiome(Holder<Biome> holder, Random random) {
        NonNullList<Integer> ElementList = NonNullList.create();
        if (holder.is(GGBiomeTags.IS_CRYO)) {
            ElementList.add(Element.E.CRYO.getId());
        }
        if (holder.is(GGBiomeTags.IS_PYRO)) {
            ElementList.add(Element.E.PYRO.getId());
        }
        if (!holder.is(GGBiomeTags.IS_NOT_ELECTRO) && random.nextInt(8) == 0) {
            ElementList.add(Element.E.ELECTRO.getId());
        }
        if (holder.is(GGBiomeTags.IS_GEO)) {
            ElementList.add(Element.E.GEO.getId());
        }
        if (holder.is(GGBiomeTags.IS_HYDRO)) {
            ElementList.add(Element.E.HYDRO.getId());
        }
        if (holder.is(GGBiomeTags.IS_DENDRO)) {
            ElementList.add(Element.E.DENDRO.getId());
        }
        if (holder.is(GGBiomeTags.IS_ANEMO)) {
            ElementList.add(Element.E.ANEMO.getId());
        }
        return ElementList.size() > 0 ? ElementList.get(random.nextInt(ElementList.size())) : Element.E.ELECTRO.getId();
    }
}