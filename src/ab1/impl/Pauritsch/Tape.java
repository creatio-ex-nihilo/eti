package ab1.impl.Pauritsch;

import java.util.Arrays;

public class Tape {

    private char[] tapeContent;
    private int headPosition;

    public int getHeadPosition() {
        return this.headPosition;
    }

    public void setHeadPosition(int headPosition) throws IllegalArgumentException {
        if (headPosition < 0 || headPosition >= this.tapeContent.length) {
            throw new IllegalArgumentException("head must be on tape");
        }
        this.headPosition = headPosition;
    }

    public char[] getTapeContent() {
        return this.tapeContent;
    }

    void setTapeContent(char[] tc) throws IllegalArgumentException {
        if (tc.length <= 0) {
            throw new IllegalArgumentException("tape content has to be something");
        }
        this.tapeContent = tc;
    }

    char[] getLeftOfHead() {
        return Arrays.copyOfRange(this.tapeContent, 0, this.headPosition);
    }

    char getBelowHead() {
        return this.tapeContent[this.headPosition];
    }

    char[] getRightOfHead() {
        return Arrays.copyOfRange(this.tapeContent, this.headPosition + 1, this.findLastNonBlankPosition());
    }

    private int findLastNonBlankPosition() {
        for (int i = this.headPosition + 1; i < this.tapeContent.length; i++) {
            if (this.tapeContent[i] == TMImpl.BLANK) {
                return i;
            }
        }
        // didn't find a blank, so copy everything
        return this.tapeContent.length;
    }
}
