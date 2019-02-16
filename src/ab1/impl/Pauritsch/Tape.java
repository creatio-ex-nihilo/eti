package ab1.impl.Pauritsch;

import java.util.Arrays;

public class Tape {

    // would be better to use a dynamic collection like ArrayList or Vector,
    // but I'm too lazy to rewrite it now.
    private Character[] tapeContent;
    private int headPosition;

    public int getHeadPosition() {
        return this.headPosition;
    }

    public void setHeadPosition(int headPosition) throws IllegalArgumentException {
        if (headPosition < 0) {
            throw new IllegalArgumentException("head must be on tape");
        } else if (headPosition >= this.tapeContent.length) {
            // resize array
            Character[] tmp = Arrays.copyOf(this.tapeContent, headPosition + 1);
            Arrays.fill(tmp, this.tapeContent.length, headPosition + 1, TMImpl.BLANK);
            this.tapeContent = tmp;
        }
        this.headPosition = headPosition;
    }

    void writeHead(Character c) {
        this.tapeContent[this.headPosition] = c;
    }

    public void setTapeContent(Character[] tc) throws IllegalArgumentException {
        if (tc.length <= 0) {
            throw new IllegalArgumentException("tape content has to be something");
        }
        this.tapeContent = tc;
    }

    Character[] getLeftOfHead() {
        return Arrays.copyOfRange(this.tapeContent, 0, this.headPosition);
    }

    public Character getBelowHead() {
        return this.tapeContent[this.headPosition];
    }

    Character[] getRightOfHead() {
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
