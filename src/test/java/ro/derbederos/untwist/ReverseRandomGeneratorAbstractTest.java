package ro.derbederos.untwist;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.math3.random.RandomGeneratorAbstractTest;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public abstract class ReverseRandomGeneratorAbstractTest<T extends ReverseRandomGenerator>
        extends RandomGeneratorAbstractTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    /**
     * RandomGenerator under test
     */
    protected T generator;

    /**
     * Override this method in subclasses to provide a concrete generator to test.
     * Return a generator seeded with a fixed seed.
     */
    protected abstract T makeGenerator();

    /**
     * Initialize generator and randomData instance in superclass.
     */
    public ReverseRandomGeneratorAbstractTest() {
        generator = makeGenerator();
        super.generator = this.generator;
        randomData = new RandomDataGenerator(generator);
    }
}
