package br.com.aceleramaker.model;

import br.com.aceleramaker.exception.ExplosionException;

import java.util.ArrayList;
import java.util.List;

public class Field {
    private final int row;
    private final int column;

    private boolean isOpen = false;
    private boolean hasMine = false;
    private boolean isMarked = false;

    private List<Field> neighboringFields = new ArrayList<>();

    Field(int row, int column) {
        this.row = row;
        this.column = column;
    }

    boolean addNeighbor(Field neighbor) {
        boolean diffRow = this.row != neighbor.row;
        boolean diffCol = this.column != neighbor.column;
        boolean diagonal = diffRow && diffCol;

        // distance between actual row/column and possible neighbor row/column
        int deltaRow = Math.abs(this.row - neighbor.row);
        int deltaCol = Math.abs(this.column - neighbor.column);
        int deltaTotal = deltaRow + deltaCol;

        // same row or column neighbor distance must be 1
        if (deltaTotal == 1 && !diagonal) {
            neighboringFields.add(neighbor);
            return true;
        }

        // diagonal neighbor distance must be 2
        if (deltaTotal == 2 && diagonal) {
            neighboringFields.add(neighbor);
            return true;
        }

        return false;
    }

    void switchMarkedField() {
        if (!isOpen) {
            isMarked = !isMarked;
        }
    }

    boolean openField() {
        if (!isOpen && !isMarked) {
            isOpen = true;

            if (hasMine) {
                throw new ExplosionException("Game over!");
            }

            if (isNeighborhoodSafe()) {
                neighboringFields.forEach(Field::openField);
            }

            return true;
        }
        return false;
    }

    boolean isNeighborhoodSafe() {
        return neighboringFields.stream()
                .noneMatch(nField -> nField.hasMine);
    }

    void mine() {
        if (!hasMine) {
            hasMine = true;
        }
    }

    public boolean isMarked() {
        return isMarked;
    }

    public boolean isOpened() {
        return isOpen;
    }

}
