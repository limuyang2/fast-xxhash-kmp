package io.github.limuyang2.xxhash.lib

/**
 * Multiplatform wrapper for xxHash library (v0.8.4).
 *
 * Provides one-shot hash functions: XXH32, XXH64, XXH3-64, XXH3-128.
 */
expect object XXHash {

    /**
     * XXH32 hash of the entire array.
     * @param input data to hash
     * @param seed  32-bit seed
     * @return 32-bit hash
     */
    fun xxh32(input: ByteArray, seed: Int): Long

    /**
     * XXH32 hash of a slice of the array.
     * @param input  data array
     * @param offset start offset
     * @param length number of bytes
     * @param seed   32-bit seed
     * @return 32-bit hash
     */
    fun xxh32Bytes(input: ByteArray, offset: Int, length: Int, seed: Int): Long

    /**
     * XXH64 hash of the entire array.
     * @param input data to hash
     * @param seed  64-bit seed
     * @return 64-bit hash
     */
    fun xxh64(input: ByteArray, seed: Long): Long

    /**
     * XXH64 hash of a slice of the array.
     * @param input  data array
     * @param offset start offset
     * @param length number of bytes
     * @param seed   64-bit seed
     * @return 64-bit hash
     */
    fun xxh64Bytes(input: ByteArray, offset: Int, length: Int, seed: Long): Long

    /**
     * XXH3 64-bit hash (seed = 0).
     * @param input data to hash
     * @return 64-bit hash
     */
    fun xxh3_64bits(input: ByteArray): Long

    /**
     * XXH3 64-bit hash with explicit seed.
     * @param input data to hash
     * @param seed  64-bit seed
     * @return 64-bit hash
     */
    fun xxh3_64bitsWithSeed(input: ByteArray, seed: Long): Long

    /**
     * XXH3 128-bit hash (seed = 0).
     * @param input data to hash
     * @return LongArray[2] where [0]=low64, [1]=high64
     */
    fun xxh3_128bits(input: ByteArray): LongArray

    /**
     * XXH3 128-bit hash with explicit seed.
     * @param input data to hash
     * @param seed  64-bit seed
     * @return LongArray[2] where [0]=low64, [1]=high64
     */
    fun xxh3_128bitsWithSeed(input: ByteArray, seed: Long): LongArray
}
