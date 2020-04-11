package com.knok16.GeneticFramework;

public interface FitnessFunction {
	int getArity();
	int run(long[] genom);
}
