package talisman.controller.board;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import talisman.Controllers;
import talisman.controller.cards.TalismanCardController;
import talisman.model.action.ActionEndedListener;
import talisman.model.board.TalismanBoard;
import talisman.model.board.TalismanBoardCell;
import talisman.model.board.TalismanBoardPawn;
import talisman.model.board.TalismanBoardSection;

import talisman.model.cards.Card;
import talisman.model.cards.CardImpl;
import talisman.model.cards.CardType;
import talisman.model.character.CharacterModelImpl;
import talisman.model.character.PlayerModel;
import talisman.view.board.TalismanBoardView;
import talisman.view.cards.TalismanCardView;

/**
 * The implementation of a basic MVC controller for a TalismanBoard.
 * 
 * @author Alberto Arduini
 *
 */
public final class TalismanBoardControllerImpl
        extends PopulatedBoardControllerImpl<TalismanBoard, TalismanBoardSection, TalismanBoardCell, TalismanBoardPawn>
        implements TalismanBoardController {
    private ActionEndedListener actionListener;
    private final Map<Card, TalismanCardView> cardViews;

    /**
     * Creates a new controller.
     * 
     * @param board the board model to control
     * @param view  the board view
     */
    public TalismanBoardControllerImpl(final TalismanBoard board, final TalismanBoardView view) {
        super(board, view);
        cardViews = new HashMap<>();
        this.getView().setCardPickupListener((c) -> this.tryCollectCurrentCellCard());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TalismanBoardView getView() {
        return (TalismanBoardView) super.getView();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void applyCurrentPlayerCellActions() {
        final int playerIndex = Controllers.getCharactersController().getCurrentPlayer().getIndex();
        this.getCharacterCell(playerIndex).applyActions();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentCharacterCellCard(final Card card) {
        final TalismanCardView cardView = TalismanCardController.createView(card);
        this.cardViews.put(card, cardView);
        final int playerIndex = Controllers.getCharactersController().getCurrentPlayer().getIndex();
        this.getCharacterCell(playerIndex).setCard(Objects.requireNonNull(card));
        final TalismanBoardPawn currentPawn = this.getCharacterPawn(playerIndex);
        this.getView().addOverlayedCard(currentPawn.getPositionSection(), currentPawn.getPositionCell(), cardView,
                card.getType() != CardType.ENEMY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setActionEndedListener(final ActionEndedListener listener) {
        this.actionListener = listener;
        for (int i = 0; i < this.getBoard().getSectionCount(); i++) {
            final TalismanBoardSection section = this.getBoard().getSection(i);
            for (int j = 0; j < section.getCellCount(); j++) {
                section.getCell(j).getActions().stream().forEach(a -> a.setActionEndedListener(this.actionListener));
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Card> getCurrentCellCard() {
        return this.getCharacterCell(Controllers.getCharactersController().getCurrentPlayer().getIndex()).getCard();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tryCollectCurrentCellCard() {
        final PlayerModel player = Controllers.getCharactersController().getCurrentPlayer();
        final int playerIndex = player.getIndex();
        final Optional<Card> card = this.removeCurrentCellCard();
        card.ifPresent((c) -> {
            final TalismanBoardPawn currentPawn = this.getCharacterPawn(playerIndex);
            this.getView().removeOverlayedCard(currentPawn.getPositionSection(), currentPawn.getPositionCell());
            ((CharacterModelImpl) player.getCurrentCharacter()).getInventory().addCard((CardImpl) c);
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Card> removeCurrentCellCard() {
        final Optional<Card> card = this.getCurrentCellCard();
        this.getCharacterCell(Controllers.getCharactersController().getCurrentPlayer().getIndex()).clearCard();
        return card;
    }
}
