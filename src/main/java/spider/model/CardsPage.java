package spider.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName CardsData
 * @Description 通过用户weibo tab的id查询的列表外壳 里面包含cardListInfo和cards
 * @date 2022/2/8 10:09
 * @Author eee27
 */
public class CardsPage implements Serializable {
	@JsonProperty(value = "cardlistInfo")
	private CardListInfo cardListInfo;
	@JsonProperty(value = "cards")
	private List<Card> cards;


	public CardListInfo getCardListInfo() {
		return cardListInfo;
	}

	public void setCardListInfo(CardListInfo cardListInfo) {
		this.cardListInfo = cardListInfo;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setCards(List<Card> cards) {
		this.cards = cards;
	}
}
