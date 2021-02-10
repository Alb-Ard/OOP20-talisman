package talisman.view.board;

import java.util.List;

/**
 * An interface for the view of a board section.
 * 
 * @author Alberto Arduini
 *
 */
public interface BoardSectionView {
    /**
     * Gets the child cells count.
     * 
     * @return the cells count
     */
    int getCellCount();

    /**
     * Gets the cell at the given index.
     * 
     * @param cellIndex the cell index
     * @return the cell instance
     */
    BoardCellView getCell(int cellIndex);

    /**
     * Gets the given cell x position.
     * 
     * @param cellIndex the cell index
     * @return the x position
     */
    default int getCellPositionX(int cellIndex) {
        return this.getCell(cellIndex).getCellX();
    }

    /**
     * Gets the given cell y position.
     * 
     * @param cellIndex the cell index
     * @return the y position
     */
    default int getCellPositionY(int cellIndex) {
        return this.getCell(cellIndex).getCellY();
    }

    /**
     * Sets another section as contained into this one.
     * 
     * @param section the section to insert
     */
    void setContainedSection(BoardSectionView section);

    /**
     * Creates a new section from the given cell list.
     * 
     * @param cells the cells that the section will contain
     * @return the section
     */
    static BoardSectionView create(List<BoardCellView> cells) {
        return new BoardSectionViewImpl(cells);
    }
}
