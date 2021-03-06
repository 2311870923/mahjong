package com.nbcb.poker.threewater.rule.judger.special;

import java.util.ArrayList;
import java.util.List;

import com.nbcb.core.card.Card;
import com.nbcb.poker.card.PokerAllCards;
import com.nbcb.poker.card.PokerCard;
import com.nbcb.poker.card.PokerCards;
import com.nbcb.poker.card.PokerUnitCards;
import com.nbcb.poker.card.PokerUnitCardsJudger;
import com.nbcb.poker.card.strategy.base.SequencePokerUnitCards;
import com.nbcb.poker.card.strategy.base.SequencePokerUnitCardsFinder;
import com.nbcb.poker.card.strategy.base.SequencePokerUnitCardsJudger;
import com.nbcb.poker.threewater.helper.ThreeWaterPokerCardsRoads;
import com.nbcb.poker.threewater.helper.ThreeWaterPokerCardsUtil;

/**
 * 
 * @author lele 三顺子 第123堆为顺子，其中QKA、A23、KA2不算
 * 
 */
public class ThreeWaterSanshunziPokerUnitCardsJudger implements
		PokerUnitCardsJudger {

	private SequencePokerUnitCardsJudger sequencePokerUnitCardsJudger;


	public void setSequencePokerUnitCardsJudger(
			SequencePokerUnitCardsJudger sequencePokerUnitCardsJudger) {
		this.sequencePokerUnitCardsJudger = sequencePokerUnitCardsJudger;
	}


	@Override
	public PokerUnitCards judge(PokerCards pokerCards) {
		PokerCards oriCards = new PokerCards();
		oriCards.addTailCards(pokerCards.toArray());
		List<PokerCards> list = new ArrayList<PokerCards>();
		boolean ret = this.judgeInner1(list, oriCards, 1);
		if (!ret) {
			return null;
		}
		List<SequencePokerUnitCards> listRet = new ArrayList<SequencePokerUnitCards>();
		for (PokerCards tmpPcs : list) {
			listRet.add((SequencePokerUnitCards) this.sequencePokerUnitCardsJudger
					.judge(tmpPcs));
		}
		return new ThreeWaterSanshunziPokerUnitCards(pokerCards, listRet);

	}

	public boolean judgeInner1(List<PokerCards> list, PokerCards pcs, int index) {
		if (index == 4) {
			return true;
		}
		pcs.sort();
		Card[] cards = pcs.toArray();
		for (Card c : cards) {
			PokerCard pk = (PokerCard) c;
			int pkNumber = pk.getPokerNumber();
			if (index == 1) {
				if (pk.getPokerNumber() == 1) {
					continue;
				}
				if (pkNumber > 11) {
					continue;
				}
			} else {
				if (pk.getPokerNumber() > 11) {
					continue;
				}
			}

			int size;
			if (index == 1) {
				size = 2;
			} else {
				size = 4;
			}
			PokerCards retPcs = pcs.findSingleNumberSequencePokerCards(
					pkNumber, pkNumber + size);
			if (retPcs == null) {
				continue;
			}
			pcs.removeCards(retPcs.toArray());
			list.add(retPcs);
			if (judgeInner1(list, pcs, index + 1)) {
				return true;
			}
			pcs.addTailCards(retPcs.toArray());
			list.remove(list.size() - 1);
		}
		return false;
	}

//	private ThreeWaterSanshunziPokerUnitCards getThreeWaterSanshunziPokerUnitCards(
//			PokerCards pokerCards, PokerCards oriPokerCards) {
//		List<SequencePokerUnitCards> listCards = new ArrayList<SequencePokerUnitCards>();
//
//		PokerCards tmpPcs = ThreeWaterPokerCardsUtil.getPokerCardsByIndex(
//				pokerCards, 0, 3);
//		listCards.add((SequencePokerUnitCards) sequencePokerUnitCardsJudger
//				.judge(tmpPcs));
//
//		tmpPcs = ThreeWaterPokerCardsUtil
//				.getPokerCardsByIndex(pokerCards, 3, 5);
//		listCards.add((SequencePokerUnitCards) sequencePokerUnitCardsJudger
//				.judge(tmpPcs));
//
//		listCards.add((SequencePokerUnitCards) sequencePokerUnitCardsJudger
//				.judge(oriPokerCards));
//
//		PokerCards copyCards = new PokerCards();
//		copyCards.addTailCards(pokerCards.toArray());
//		copyCards.addTailCards(oriPokerCards.toArray());
//
//		return new ThreeWaterSanshunziPokerUnitCards(copyCards, listCards);
//	}
//
//	private PokerUnitCards judgeInner(PokerCards oriPokerCards,
//			PokerCards pokerCards, ThreeWaterPokerCardsRoads roads) {
//
//		// System.out.println(pokerCards + " " + oriPokerCards);
//
//		if (pokerCards.size() == 8) {
//			// System.out.println(pokerCards);
//			PokerCards tmpPcs = ThreeWaterPokerCardsUtil.getPokerCardsByIndex(
//					pokerCards, 3, 5);
//
//			PokerUnitCards pucs = sequencePokerUnitCardsJudger.judge(tmpPcs);
//			if (pucs == null) {
//				return null;
//			}
//			pucs = sequencePokerUnitCardsJudger.judge(oriPokerCards);
//			if (pucs == null) {
//				return null;
//			}
//			return this.getThreeWaterSanshunziPokerUnitCards(pokerCards,
//					oriPokerCards);
//		}
//
//		Card[] cards = oriPokerCards.toArray();
//		for (Card c : cards) {
//
//			pokerCards.addTailCard(c);
//			oriPokerCards.removeCard(c);
//
//			if (roads.walkedByPokerNumber(pokerCards)) {
//				pokerCards.removeCard(c);
//				oriPokerCards.addHeadCard(c);
//				continue;
//			}
//
//			if (pokerCards.size() == 3) {
//				PokerUnitCards pucs = sequencePokerUnitCardsJudger
//						.judge(pokerCards);
//				if (pucs == null) {
//					pokerCards.removeCard(c);
//					oriPokerCards.addHeadCard(c);
//					continue;
//				}
//
//				SequencePokerUnitCards first = (SequencePokerUnitCards) pucs;
//				if (first.getSequenceMin() == 1) {
//					pokerCards.removeCard(c);
//					oriPokerCards.addHeadCard(c);
//					continue;
//				}
//				if (first.getSequenceMin() == 12) {
//					pokerCards.removeCard(c);
//					oriPokerCards.addHeadCard(c);
//					continue;
//				}
//			}
//
//			roads.addPokerCards(pokerCards);
//
//			PokerUnitCards pucs = judgeInner(oriPokerCards, pokerCards, roads);
//			if (pucs != null) {
//				return pucs;
//			}
//
//			pokerCards.removeCard(c);
//			oriPokerCards.addHeadCard(c);
//		}
//		return null;
//	}

	public static void main(String[] args) {

		SequencePokerUnitCardsJudger sequencePokerUnitCardsJudger = new SequencePokerUnitCardsJudger();
		SequencePokerUnitCardsFinder sequencePokerUnitCardsFinder = new SequencePokerUnitCardsFinder();

		ThreeWaterSanshunziPokerUnitCardsJudger j = new ThreeWaterSanshunziPokerUnitCardsJudger();
		j.setSequencePokerUnitCardsJudger(sequencePokerUnitCardsJudger);
		// j.setSequencePokerUnitCardsFinder(sequencePokerUnitCardsFinder);

		PokerAllCards pokerAllCards = new PokerAllCards();
		pokerAllCards.start();

		PokerCards pcs = new PokerCards();

		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.FANGKUAI, 12));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.HEITAO, 13));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.HEITAO, 11));

		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.CAOHUA, 10));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.CAOHUA, 11));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.CAOHUA, 12));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.CAOHUA, 13));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.CAOHUA, 1));

		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.HONGTAO, 11));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.HONGTAO, 10));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.HONGTAO, 12));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.HONGTAO, 13));
		pcs.addTailCard(pokerAllCards.getPokerCardByColorAndPokerNumber(
				PokerCard.Color.HONGTAO, 1));

		System.out.println(j.judge(pcs));
	}

}
