package ro.derbederos.untwist;

public class TurboPascalRandomGeneratorTest extends ReverseRandomGeneratorAbstractTest {

    @Override
    protected ReverseRandomGenerator makeGenerator() {
        return new TurboPascalRandom(0xC44002DC);
    }

}
