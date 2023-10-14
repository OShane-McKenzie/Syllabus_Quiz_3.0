package com.syllabus.pq

import android.annotation.SuppressLint
import org.apache.commons.compress.compressors.CompressorStreamFactory
import org.apache.commons.compress.utils.IOUtils
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.util.*
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

/**
 * The DataEncode class provides functionality for encoding and decoding strings
 * using a custom encoding scheme. The class also provides a utility method for
 * decoding Base64 encoded strings.
 */
class DataEncode {

    fun getKeys(key:Int):String{
        val dbAuth = """
    &~_~&_-~.&~_-&~_-_&~--.&~.&-_~_.&~-~&~-__&@@@&~_-_.&-_~_&--~&~_--.&-_.&-~_.&-_-.&@@&~_-&-~~&~--.&@@@@&-_~_.&--~.&_--~.&~.&~_-&~_-.&~_~&~_-_&~--.&~-_-.&_--~.&~_--.&-_.&-~-&--~&~--&~--.&-_~_&~~-.&@@&-_-.&~&~~-.&~--&-_-.&~&-_.&@@@@&~_-&@&~.&_-~.&-_.&@@@@&-_~_.&~-~&-_~_.&-~~.&~_~&-~_.&-_.&~_--&~_~&@@@&~-__&~__~&-_-.&-~.&~_-_.&-_.&~_~&@&@@&~_-.&~_-_.&-~_.&~_-_.&~-_-&~--.&~-__.&-_.&_-~&~-__&~--&-_~_.&_--.&~_-_.&~_-_&--~.&~-&-~_&-_~.&-_.&--.&-~_&_-~.&-_.&-_.&-_~_.&-_~.&-_~&@@@@&~-_-.&_--&-~~&-_-.&~-&_--&~--.&@@@@@&--~&@@@@&-~.&~_--&-_.&~.&-~~&_-~&-_.&@@@@&~_--.&_-~&-_~_.&~.&-_.&~__~.&~-__&@&-_-.&-_&~--.&_--&@@@@@@&_--.&~_~&~_-.&-~.&_-~&_--~.&~.&_-~&~__~&~--.&_--&_-~&~-_-&~~-.&~_-_&~_-&-~.&-_.&@@@&@@@@@@&_~~.&-~_&_-~.&-_.&~~-&~_~&~_-.&-~-&~__~&~_~&-_~.&-~_.&@@@@@&~_-_.&_-~.&~__~&-_&~_-_.&~_-_&-~-&-~_.&-_.&-~~.&-~.&-_-.&~__~.&~_-.&~_-_.&@@&~.&-~_.&-_.&~-_-&--~&~--&-_-.&@@&~.&~_--.&~-~&~__~.&--~&~.&-_-.&@@@@@&~~-.&-~_.&-~_.&@@&-_~_.&~--&--~.&-_.&-~_&_-~.&-_.&~--.&-_~_.&@@&~--.&@@&-_-.&-_~_.&~_-&~~-&~~-.&-~.&-_~_.&@@@@&~--.&_~~&~-__&~_--.&-_-.&~-~&~_-&--&-_-.&~.&~-~&--~.&_--~.&~.&_~~.&~-_-.&~_--.&~_-_&~_-_.&-_~_.&-_-.&@@@@&-~-&~-_-.&-~~.&@@@&_~~.&-~.&~.&~-~&-~-&~_--.&~_--.&@@&-_.&-_-.&--~&~-__.&--.&~&-_-.&@@@&_--~.&@@@@@@&~~-.&~-__.&~-__&~-~.&-_.&-_-&~-__&_~~&~_~&_~~&~-__&-~_.&~~-.&@@@&@@@@@@&~&~~-.&-_~.&_-~&~-&~~-.&@@@&~-~&@@@@@&~-&-_~.&_~~.&-~.&~.&-_~.&~_-&-_.&-_-.&_~~&~_-_.&~_--&_--~.&~.&-_.&--.&~-~.&@@&~--.&~-_-.&~-&@&~_-_.&_--&-~_&-_-&~&@&-_.&_--&_-~&@@@&-_.&@@@@&-_.&@@@@@&-~~.&~--&~--.&~_-.&_--~.&-_-&_-~&~_-_.&~__~.&-_~_.&_~~.&--&~_-_.&~-__.&--~.&~_-_&~-__&~_-_&_~~.&~__~.&-_~_.&~-__.&-_.&~-__.&~_-_.&-_~.&@@@@@@&--~.&~-&@@@&~_-_.&-_~.&~-&~_--.&~-~&_-~.&~_~&-~_.&--.&@@@&-_~_.&@@@&-~~&~__~&~_~&@@@@&-~-&~_-&~-_-.&@@@&@@@@@@&~-_-&-~~.&-~~.&~--.&-_&-_-.&~_--.&-~.&~__~.&~_~&~_--.&~&@@@@@&-_.&~~-.&@@@@@@@@@@&_--~&~-_-.&@@@&-_~_.&~_-_&-_.&-_-&-~~&~.&-_.&@&@@@@@@@@@@&_--~&~~-.&-~_.&-_~&-_&-~~.&-_~.&_-~&-_-&--~&@@@@&~-__&-_~.&_--~.&_~~&~-_-.&@@@@@@&-_.&-_-&-~_.&~&-_-.&~-~&-_~_.&-_-&--~&@@@@&-_.&-~-.&-~~.&@@@&-_.&@@@@&_--~.&-_~_.&-_.&~~-.&-_~_.&~_-.&~--.&_~~.&~_-_.&~_--.&@@@@@@@@@@&@@@@@@&~_~&~.&@@&-_&~__~.&~.&-~-&-_~_.&~-__&_-~.&-~-&_-~&~_-_.&~_--.&~_-&-~.&~-&-~_.&_~~.&~-~&--~&-~_.&_~~.&~-__.&~.&~-~&~__~&@@@&-~_&-~_.&~_~&@@@@@@&~_-_.&-~.&~-~&_-~&~--.&@@&~-__&_~~.&-_.&~-~&-~.&~_-_.&-~_&--&-~~&_--~&~-~.&@@@&~-__&-_-.&~.&~~-.&-~~&-_&~_~&_-~.&~-~&-_~_&~_--.&@@@&_~~.&-~_.&~_--.&~.&-~.&--.&_--.&@&-~.&~-_-&-~_&-~_.&_~~.&-~-.&~-&~_-.&~_--.&@@&-~_&~_-_&_~~.&@&-_-.&-~.&~_-_.&-_~.&~-~.&@&-~.&-~_.&~~-.&@@@&_-~&~-_-.&~-__&-_~_.&~--.&-~-.&~_-_.&@@@&-_-.&~&--~&-_~.&~_-&~&~_~&@&--.&-_&~-__&~-__.&-_.&~_-&~-__&@@@@&-~-&-_-&-~_&~-~&-_.&-~~&-_~_.&-_-&@@@@@@@@@@&@&~_-_.&_--~&@@@@@@@@@&@&-~~.&-_~_.&_~~.&~--.&~--.&@&~_-_.&~_~.&-_~_.&~_--.&@@@@@@@@@@&~-_-&~-__&@@@&-~-&-_&~~-.&~-__.&~_-&@&~_~&-_-&_--~.&@&~.&-_-&-~~&~-~.&~-__&_~~&~-.&@@@@&~_~&-_~.&_~~.&-_.&-_.&@@@&-_-.&@&-_-.&~_-_&-_.&-~-.&~~-.&-_~.&-~.&--~.&-_~_.&@&@@&~--.&~-&_--~&-~~&~_-&-~_&-_~.&~-__&~_-&-~~.&~--&--~.&~--.&~_-_.&@@@&@@@@@@@@@@&--&~_~&@@@&-_.&~__~&~.&-_~.&~~-&~--&-~~.&-_-&~_-&~__~.&-~_&_~~&~_~&_-~&~_-_.&@@@@&--.&-_&-_-.&~_--.&~&-_&~.&_--&~-~&~__~.&~-_-.&~.&_~~.&_-~.&~~-.&~--&_--~.&~--&~_--.&~-~&-~-&~&~_~&_-~.&~__~&_-~&-_~_.&-~~.&-_~&@&~-_-.&@@&_~~.&~-&~-_-.&~_--.&~-__&~_~.&-~_&-_~.&-~~&~_-.&~--.&~_~.&-~~&~-&~_~&~.&-_~_.&~__~&--~&~.&@@@@@@@@@@&_--~.&~.&-_~.&-~~&@&~-__&-_~.&-_~_.&@@@@@&-_-.&~_~.&-~~&@@&~-&_--&-~.&~_-.&~--.&-~~.&--~.&_~~&~-~.&@&-_.&~--&-~~.&_--&-_~_.&~-__.&~_~&~.&~_~&_--~&-~~.&~.&_~~.&~~-.&-_-.&-_-&-_.&@@@&--~&@@@@&~-__&_~~.&~_~&@@@&@@@@@@@@@@&~__~.&~_-_.&-_~_.&-_-.&@@&~_-_.&@&~-__&_-~&--~&-~_.&-~.&-~.&~~-.&~-~&~_-_.&~_-.&~_-_.&~_--.&--.&~&--~&-_-&~-__&_~~&-_.&@@@&-~-&-~~.&~_--.&~.&-_~_.&_~~&_--~.&_--&-_.&-~-&-~~.&_--&@@@@@&_--~&~-~.&@@@@&~_~&@@&~-__&~-__.&-~.&~_-_.&-_-.&~-~&-~.&-~-.&~~-.&~-__.&-~.&@@@&~~-.&-~.&~--.&@@@&~-&@@@@&~_--.&@@&-_-.&~_--.&~--.&@@@&-_.&_~~&-~.&~-~&~--.&@&_~~.&_-~&~~-.&-~~.&_--&@@@@@&-_.&_-~.&_~~.&@@@@@@@&--~&-~_.&-~_.&@@@&~~-.&~_~.&@@@@@@@@@@&~-__.&~-&-_-&~-~&_-~.&-~~.&@@@@&-~.&-~.&-~~.&_-~.&~_-&~_-_.&~-&~-~&-~.&--~.&~-&~_-_&-_.&~-~&~~-.&~_-.&-~-&~-__.&~-__&@@@&~--.&_-~.&-_.&~_--.&~_~&~&~__~.&~.&@@@@@@@@@@&-~_.&~__~.&~_-.&~__~&@@@@@@&--~&~-~&~_-&_--&~-&-~.&-_.&~-_-&-_~_.&~--&~_-&~_-_.&~_~&~-~&--~.&~-~&~-&~_--.&@@@@@@&-_~_.&~.&~-~&~-~&_-~.&~_--.&-~.&~_-&-_.&~--.&~_-_&-~.&@@@@@@&~_-_.&~_-_&~--.&-_~_.&~_--.&~-__.&_--&@@@@&-~_&~-~&-~.&@@@&~_-_.&_--&-_.&@@@@&~-__&-_~_.&~--.&@@@@@&_--.&~--&~_-&~-_-.&~-&-_~_.&~-__&_--&~_~&~-~&-~.&~__~.&--~&-_~_.&-_~_.&~&_--~.&-_~_.&-_~_.&-~-&~_~&@@@&-~~&~-_-.&~_--.&~~-.&-~~&-~-&_--.&@&-_~&_-~&~_--.&-_-&-_~_.&~_-_&-_.&@&@@@@@@&--&~__~.&~_-.&~-__&-_.&~--.&_--&~--.&~-__.&~~-.&-_~.&-_.&--~.&--~&~-__.&_~~.&-~-&~~-.&~_-.&~-~&_~~&--~&~_~.&-~~&-_~.&-_-.&~_-.&~--.&~--.&~_--.&~_--.&~--.&@&~-__&-~.&~_-&~_--&-_.&~_-_&~--.&--.&~_--.&@@&-~.&~~-&~.&~-__.&~-~&_-~.&-_~_.&_--&@@@@@@&~-_-.&~-__&-_-&@&~&~_--.&--&@@@@@@@@@@&_~~&-_-.&-~.&_~~.&-_&-_~_.&~--&-_~_.&-_&~~-.&-_-&~_-&_--~&-_~_.&~-~&-~.&~_--&-_-.&~_-_&-_.&-_&_--~.&-~~.&~_~&-_~_&~--.&~-~&~-__&~_-&~~-.&@@@&~_-&_--~&--~&@@&--~.&~&-_~_.&~_--.&_-~&~--.&~-&-~_.&-_~_.&_-~&_--.&@&_~~.&--&-_~_.&~-__.&-_-.&~&~~-.&_~~&_--~.&_--~&~_~&~--&~-~&~.&~.&~_--.&~--.&_--&~-_-.&@@&~_-&@@&-~~.&~.&~_-_.&@@@@&-~~.&-~.&_~~.&-_-&~-__&_--&~_-_.&@@&-_.&@&@@@@@@@@@&-_~_&_--.&@&-~-&~~-&~__~.&_-~.&-~-&-~~.&~-&~--&~-.&~&_--~.&-_~_.&-_-.&@@&~__~.&-~.&~-__&~_~.&-_~_.&-~.&--~.&-~-.&~.&_--&-~.&~__~&~-_-.&~.&-_-.&@@@@&_--.&@@@@&~_-_.&-~-.&~-&@@@&-_.&~_-_.&--~&_~~&_--&_-~&~_~&~--&~-.&-_&-_.&@@&_~~.&-_-.&~_-_.&~_--.&-_.&~_--.&~--.&@&~-~&@@@@@&~_--.&~_--.&@@@@@@@@@@&_--.&-_~_.&-_~.&~-__&_-~&~-__&-_~_.&~__~&~--&~_--.&-~~.&-_~_.&@@@@@@@&~_--.&-~_.&~&_--~&-_.&-_-&-_~_.&~_-.&-~~.&~.&~_-_.&-~~.&~_-_.&@&-~_.&@@@@@&~_-_.&~_--.&-~-&-~_.&-~~.&@@@@&_~~.&-_-.&-_-.&@@@&_--~.&~--&-~~.&-_~.&-_.&~_~.&~-_-.&@&~__~&~--&_--~.&~~-.&-~~&-_.&-~~.&_-~.&~_-_.&~_--&-_~_.&~_--.&_~~.&~~-&-_-.&-_-&-_-.&@@@@@&~-_-.&_--&@@@@@@@@@@&-_-.&-_.&~-~&~-.&@&~--.&-_-&@@@@@&@@@&~_--.&-_-&@@@@@@&@@&~.&~-__.&~_-_.&-~~.&~_~&@@@&-_.&_--.&~-~.&@@@&~_-&~-_-.&~__~.&~_-.&-~-&~~-&~_~&@@@&~--.&@@@@@&~.&~_--.&@@@@@@&~_-_&--~&@&-~-&~.&~_~&~--&~_--.&@@@@@&~_~&-~_.&-~~&-_-&~-&-~_.&-~~&-~~.&~--.&@@@&-_~&@@@@@&~~-.&_--&~-__&@&~-&--&-~~&~_--.&-_.&_--&@@@@@@&~_-&~_~&_--&@@@@@@@@@&_--~&_--~.&~.&-_.&~~-&~~-.&_~~&-_.&-~~&~.&-_-&~~-&_--~&~-_-.&~_-_&~_-&~-&~_-_.&-~~.&~_-_.&@&~_--.&@@&-~-&-_-.&~_-_.&-~_.&~--.&-_-&~_--.&_--&@&@@@&~_~&-_~_.&~-.&_-~&-_-.&~--&~_-_.&~_-.&-_-.&~--&~--.&~-&~-__&-~_.&-~-&~_-&~.&_~~&~--.&--.&~_-_.&~-~&--~.&_--~&-_.&_~~&-_.&@&~--.&@@@@&~_-&-~~.&-_~_.&~-__.&~-__&@@@@&--~&~-__.&~-~&-~_.&~_~&-~_.&-~-&@@&~_-_.&~_--.&@@@@@@@@@@&-~~.&-_-.&-_~.&_--&~--&-~_&_-~.&~-__&@@&-~~.&@&@@@@@@@@@@&@@@@@@@&-_-.&~~-.&-~~&--~.&~.&~-__.&_~~.&-_~_&~_~&_--&~_-_.&~-~.&_--~.&@@@&-~.&_--.&-_-.&~_-.&-~-&~~-&~_~&~-__.&_~~.&_-~.&~--.&~_-.&~_-_.&~_-.&-~_&_-~.&-_~_.&~&-~~.&~-~&-_~_.&_~~.&~_--.&@&~_-_.&-_-.&~--.&-~.&-~-&--&-_~_.&-~~.&-_.&~-_-&~_~&@@&-~-&~-&~-&~_--.&-_~_.&-~~&~-&-~~.&-~_.&@@@@@&_--~.&@&@@@@@@&_--.&--~&~.&_~~.&~__~&--~&~_--.&-_.&~_-.&-~_&_-~.&--~.&~__~&-~~.&~_--.&~_-_.&~_~.&--~&@@@&-_~_.&~_~.&_--~.&_~~&_--&~--&-_.&~_-_&~-~&~~-.&-~_&-_-&~_-&_--&-_-.&_--&_~~.&~_--&~.&~_--.&~-~&_-~&~_-_.&_--&@@@@@@@@@@&_~~&~_--.&_~~&_~~.&@&~-&~--&~-~&-_-&~.&-_~_.&-~-&-_&-_-.&-_-&-~~&_--.&-_~_.&-~_.&@@@@@@@@@&_--~&~--.&-~~.&_~~.&@@@@&~--.&~_-.&~-.&@&~-__&_--~&@@@@@@@@@@&~_-&--~&~-~&_~~.&_--~&-_.&@@&-_-.&_-~&-~~.&-~_.&-_~_.&-_~_.&-_-.&-~~.&-_.&@@@@@@&-~_&~-~&-_~_.&~_--&~--.&-_~_.&--~.&~-~&~--.&-_~_.&~-_-.&@@@@&~_-_.&-_~_.&-~_.&@&~_~&~_-_&-_.&~&--~&~-__.&~--.&~&-_.&-~.&~_-&~-&-_.&~--&_~~.&~&-_~_.&-~.&-_~_.&~&~--.&-~~.&-_-.&_-~&-~_&~.&-~-&@@@&~-__&-_~.&@&@@@@@&~-_-.&_~~&-_~_.&@&-~~.&_-~.&~_-&-_&-~~.&-_-&-~-&~-_-.&-_~_.&_~~&~--.&~&~-_-.&~_-_&~--.&_--~&~-__&-_-&~_-&--.&--~&~.&~--.&~_--&~_-_.&~.&@@@@@&-_&~--.&@&~-~&@@@@&-_-.&_~~&~-~&~~-.&~~-.&~-~&~--.&-~.&~~-.&~.&~--.&-_&~~-.&-~_.&-~~&@@@@@@&~--.&_-~.&~-__&~~-.&-_.&@@@&~_-&~_-_&-~_&-_~_.&-~_.&-_~_&-_~_.&~--&~_-_.&_-~.&~_-_.&~.&-_~_.&~__~.&-_-.&@@@&~-~&@@@@@@&~--.&~-~&~_-&@@@&~_~&@&~--.&~__~.&-~_&_-~.&_~~.&~_-.&~__~.&~.&-~~&~&_--.&@&-_.&~-~&~-&~_--.&-_~_.&-_~_.&~-__&~_--.&~-__&_--~&--~&~-~&-_.&_~~&~_~&~_-.&~_-_.&~_-_&-_.&@@@@&-_~_.&-~~.&~.&~_--.&-_.&~_--.&--~&~.&~--.&~_--&~-__&~_-.&--.&@@@@@@&~_-_.&~-__.&-_~_.&-_-&_--~.&_~~&-_~_.&-_-&~--.&~_--.&~--.&-~-&_--~.&@@@&_-~&~__~&~--.&-_-&-~~&~_~.&~-&~_-_&~-~&--.&-~~.&@@@@&-_.&~_-_&~_~&-~~.&~-~&_~~&~_--.&-_-&-_~_.&-~_.&~_-_.&~_-.&~--.&-~~.&~-__&~-__.&~_--.&-_&_--~.&-_-&-~~&_--~&-_.&@&~-__&@@@@@@&~~-.&~.&@@&~-~&~_--.&@@@@&-~-&-_&~.&~-~&-_~_.&~&~-__&@@@@&-~-&_-~.&~_--.&@@@&@@@@@@&-~_.&_--~.&-_~_.&~_-&_~~.&_--~.&@@@@&_~~.&-~-.&-~~.&@@@&-~.&-_~_.&-_~_.&_-~.&~--.&-~~&_--~.&-_~_.&-~.&_--.&-_~_.&~_--.&-~.&@@&~-__&@@@&_-~&~_--&-~~.&@@&-~-&-_~_.&_--~.&@@@&-~.&@@@@@&~_-_.&_-~.&--~.&-_&~_-_.&~_-.&-_.&-~-&-~_&~.&~-__&--&-~~.&-~~.&~-~&-_~_.&-_~_.&-~_.&~--.&@@@@@@@&~--.&~_~.&~~-&@@@@&--~&~-~&~_--.&~&-_~_.&-_~.&~-__&-_-.&~~-.&-~_.&-~.&~~-.&~--.&-_-&-_~_.&~--.&-~_&~-~&-~.&-_~.&~-&_--&~_-&-_-&--~&-_~.&~-~&@@@@&~-&_~~&~_-&--.&~_~&_--&@@@@@@@@@&-_~_&-_.&~-~&-~-&-_-&-_-.&~-__.&-_.&-~~&-_.&~-__.&~_~&@@&~_~&-~.&~_-_.&-~.&~--.&~_-.&~--.&~_--&-_~_.&~-__.&_--&_--~&-_-.&_~~&~--.&~_~.&~-_-.&~--&~_~&_--~&~_--.&~-~&--~.&~_-_.&~__~.&-_~.&~_-_.&-~~.&-_.&_-~.&~-~&-_-&--~&_-~.&-~-&~_--.&~-~.&@@@&_--~.&@@@@&~_-_.&_~~&-_.&~__~&_--.&~--&~_-&-~.&-_~_.&-~.&~-__&~__~.&~--.&~-~&~_--.&@@&~_~&@@&-_-.&~--&-_.&-_-&~&@@@@@&_--~.&_~~&~_~&~--&-_-.&@@@@&--~.&_--~.&-_~_.&@@&-_~_.&-_~.&~--.&_--&~__~&@@&-_~_.&_--&@@@@@@@@@@&~_~.&--~&_-~.&-~-&~__~.&-_~_.&~-~&-_-.&@@@@@@&~-_-.&_--&@@@@@&~--&-_-.&~-__.&~--.&~_~.&_--~.&~-~&-_~&@@@@&-~_&-~~.&~-__&~~-&-~~.&-~~.&~_-_.&~-~&~.&-_-&-_~_.&~-_-&~__~.&_-~.&-~-&@@@&~_--.&@@@&~-__&~_--&_--~.&@@@&@@&--.&~-&@@@@&-_~&@@@&~_~&-_-&-_.&-~-.&~.&~_-.&~_~&_-~&~-_-.&~.&-~-&~-__.&~__~.&-~~.&~_-&@@@@@@@&~_~&-_-&~--.&~_-&~.&-~~.&-~.&~~-&~~-.&~-__.&~-__&-~-&~~-.&~--&_~~.&~&~-__&@@&~_~&@@@&~_--.&~-__.&--~.&_--.&~--.&~_--.&-~.&~.&~_--.&~-__.&~-__&-_-.&~_--.&-~_.&@@@@@&_-~&-~~.&_--~&@@@@@@@@@@&@@@@@@@&~-_-.&@@&--~.&--.&~~-.&_-~.&~-_-.&_--~&-_-.&@@@&-~.&_~~&-_-.&-~~.&-_~_.&_-~&_--~.&_~~&~-.&_--~&~_~&~.&@@@@@@&_--.&--~&@&@@@@@@&-_~.&~--.&@@@&_~~.&@@&~~-.&-_-&-~~&_--~&~--.&~.&~_-_.&_-~&~_~&-_-&_~~.&--~.&~--.&-_-&~-_-.&~--&~_~&~_--.&-_~_.&@@@@@@@&~-&_-~.&-~.&@@@@@@&-~~.&~-__.&~-__&~&~-_-.&@&~--.&@@@@@@@&--~&~.&@@@@@@&-_~_&~_--.&_~~&-_~_.&-_.&~-__&~_--.&~__~&@@@&--~&~--&~-~&_-~.&-_.&~_-.&~-~&~__~&~__~.&-~_.&~-~&~__~.&~-&~_--.&@@&~__~.&--~&~_--.&_--&@&_--.&@@&~-__&--&-~_&~_--.&_~~.&@@@@@@&-~~.&-~.&~_-&--&~_--.&-_~.&@@&_--~&-~~.&@@&--~.&_--&-~~.&~-__.&--~.&~__~&-_.&@@@&@@&~__~&-_-.&-~_.&~_-&~_-&~-__&~.&--.&@@@@@@&--~&-_~.&@@&~~-.&-_-.&_--&~-__&~-~.&-~_&~.&-~~&@@@@@@&~_~&@&-_~_.&~-_-&~-&@@&-_~_.&-_~.&~-&-~_.&@@@@@@@@@&@@@@@&~-_-.&@&~_-_.&-_~.&-_.&_-~.&--.&~--&-_~_.&-_-&@@@@@@&_--~.&-~~.&~_-_&-~_.&~--&-_~_.&_--&@@@@@@&~_-&-_-.&-_~.&-~-&-_-&-_~_.&~_-.&--~.&~&~-~.&@@@@&~--.&-~.&-~~.&@&-_~_.&_-~&-~_&~-~&~-~&-_&~-_-.&_--&~--.&~__~.&~~-.&_-~.&~--.&-~_.&-~~.&-~~.&_--&~&~__~.&-~_.&~-~&-~~&_--~.&-~~.&~-~&-~~&-_-.&_--&@@&_~~&--~&-_~.&_-~&-~_.&--~&_~~&-~.&~-&~__~.&-~_.&~-_-.&@@@@@&~_~&-~_.&~-__&~-_-&-~_&-_-&@@&--&-_.&@@@@&~_--.&_-~&~-_-.&~_--.&~_-_.&~-~&~--.&~_-.&~-.&@@&~_~&~.&@@@@@@@@@@&~__~.&~_-_.&_--&~-__&@@@@@@&~.&-_~.&~&~&-_~_.&-_~.&~--.&~&-_-.&~_-_&_--&@@@@@&_--~.&~~-.&-~~&~_~.&-_-.&@@@&~_-_.&~-__.&~--.&~.&-~_.&~--&~-__&~-__.&~-_-.&@@&-_-.&-~~.&_~~.&~_-_.&-_-.&~_-_&~-~&_--~.&--~&@@@&@@@@@@&~_--.&~~-.&~-__.&_~~.&_~~.&-~_&_--&_-~&~--&-_-.&~_--.&@@&~-_-&-_.&_--&-_.&_--.&-~_&_-~.&~--.&~_-&~-__&-~.&~-~&~_-_&-_-.&@@@@&~_~&@@@&~-&@@@&-_-.&~--&--~&@@@&~--.&--.&--~&_~~&-~_.&~&~-&~-~&--~.&~--&~_~&-~_.&~-_-.&@&-_.&_~~&-_.&@@@@@&~__~.&-~_.&~-~&~_~.&-~~.&_~~&~--.&~_-&-_~_.&-_~_.&-_~_.&~__~&~~-.&~_-_&~--.&@@@@@@&~_-_.&_--&~_-&@@@@@@&--~&_--&_~~.&@@@@&~-__&@&~-~&~-_-&~_-_.&_--&~_-&~_~.&-~~.&~.&@@@@@&@@@@&~-_-.&~--&~-__&~_--.&~-__&~-~&-~-&_--~&~-&@@&~-~&@&~.&~-~&-_~_.&~-~&-~~.&@&@@&~-&~~-.&-_-&@@&~~-.&~-__&~_-_&-_-.&_--~&~~-.&~.&@@&-~.&_--~.&@&-~_.&@@@@@@&-~~.&_-~.&~_-_.&~_-&--~&@@@&-_~_.&--~.&--~&~_-.&--.&_--~&~-&~_-.&-~_.&-_~_&-_~_.&-_-&@@@@@@@@@@&-~~.&-~~.&~--&~_-&~_-_&~__~.&-_~.&-_~&@@@@&-~~.&@&~-__&~__~.&~_~&@&~_--.&@@@@@@&~-&~-__.&~-__&~__~&--~&_--&-_~_.&-_~_&~__~.&_-~.&-_~_.&--~.&~~-.&@@&-~.&@@@@@@@&~-__&~_--.&_-~&_-~.&~__~.&-_~.&-~-&-_~_&~--.&-_~.&-_~_.&_-~.&-_-.&~-__.&-_~_.&@&~__~.&-~~.&~-__&~_-&-_.&@@&--~.&~-_-.&~_--.&@&~-~&-_&-_.&@&_~~.&~&_--~.&~--&~-__&~-__.&--~&-~.&--~.&_--.&~~-.&_--&@@&~_-_&~_--.&-~~.&~-~&@&--~&~-__.&~_-&@@@@@@&~_-_.&~-~&-_.&~_-_&~~-.&--&@@@@@@@@@@&--.&_--~.&--&-~~&~__~&--~&~_-.&-~_.&_-~&~-_-.&~_-_&--.&_-~&~~-.&~-~&~_-_.&~-_-.&-_-.&~.&_~~.&~~-&~_-_.&~-__.&~_--.&_-~&~-__&-_~.&-~~&-_.&~--.&_-~.&--~.&~_~.&-_.&-_~.&-~~&~--&-~~.&-_~_.&-_~_.&_-~.&~-__&-_~_.&~-.&@@@&~_-_.&-~_.&~-__&-~-&~_--.&-_-&~-__&-_~_.&~--.&~_-.&~-__&~_-_.&~__~.&~.&-_.&~_-_.&_--~.&@@&~-~&_~~&~_~&~_-.&~-__&_--&~.&-_~.&-~.&~_--&~-&-_-&~_-&@@@@@@&~-&@@@&@@@@@@@@@@&-~_.&--~&~--&-~.&-~-.&~-&-_-&-_.&~__~.&~-_-.&@@@&~_-&_~~&~-_-.&@@@@&~-__&~__~&-_.&@@&~-~&~_-.&_--.&@@@&-~_.&_-~&-_-.&-_~_.&-_~_.&~&~~-.&@@&--~.&~--&-_~_.&~--&--~.&@@@&-~~.&~-~&-_.&@@@@&-_.&@@@&~_-&--.&~~-.&-~~.&-~.&~_-&~~-.&@@&-_.&~_--&~-__&~_--.&_-~&-_&~_--.&_-~.&-_.&_--.&~-~.&@&~_-&_--~.&-_.&-_~.&_-~&-~.&-_-.&-~~.&~_-_.&@@&~-__&~_--.&-_.&~&-_~_.&~.&~--.&_--&-_-.&-~~.&~_-&~-_-&~__~.&~.&@@@@@@@@@@&_--.&~~-.&@@@&_--&@@@@&~__~.&~-__.&-_.&-_~_.&~-_-.&@@@&~-~&-_-&~__~.&_-~.&~_-&-_~_.&--~&@@&-~-&~~-.&~_--.&~_--.&~-__&-_.&~_--.&@@@&-_~_.&~.&~_-_.&~_-.&-_~&@@&~~-.&_--&-_~_.&~__~.&~.&~-~&-_.&-_&-_.&-~~.&-_~_.&~-_-.&~-&_-~.&-~-&~_-&--~&~_~.&-~~&~-~.&-_.&~_-.&~-~&~-__.&~--.&~_--.&~--.&-_.&~~-.&~_-.&--~.&_--~&-_.&-_~.&@@@@@@&~-~&~-__&-_-&-_~_.&-_~_.&~-_-.&-_~_.&-_.&~-_-&-_~_.&~~-.&@@@@@@@@@&@@@&~~-.&-_~.&-~-&-~-.&~.&-_-&~-~&~--.&~-__&~_-.&-_~_.&_--~.&-~~.&~_-_&~-_-.&-_~_&~.&-~.&-_.&-~-&-_-.&-~_.&~-~&@@@@@@&~_-_.&@@&-~_.&@@@&-_-.&~_-.&-~.&-~.&~_--.&~.&~_-&_--.&~_~&@&@@@@@@&~-_-.&-_.&_-~.&-_~&@@@@&--~&~_--.&~--.&_-~&-_.&_--&-_~_.&~_-.&-~~.&-~.&-_~_.&~.&--~&~_--.&@@@@@@@@@@&~_--&~__~.&-_~_.&~-__&-_.&-_~_.&_-~.&~-~&_--~.&-~_&_--&@@@@@@&~.&-_.&_--&~-_-.&@@@@@&~_-_.&--&@@@@@@@@@@&~-&-_-.&@@&-_.&~--.&-_-.&~--&_~~.&_--.&~.&-~~.&-_~&@@@@&~_--.&~-__.&_~~.&_-~.&~_~&@@@&~--.&-~-.&-_.&~.&@@&~&-~_&_-~.&-~-&@&~-~.&@@&-_.&-~~&~_--.&@@@&_~~.&--&-_~_.&~_--.&~--.&-_&-_-.&-_~.&@@@@@@&_--~.&~.&_-~.&~--.&-_~.&_--.&~--&--~.&_--.&~_-_.&@&~_-&@@@@@@@&-_.&-~.&~-__&-~_.&~__~.&~_--.&@@@@@@&~_--.&-_~_.&~_--.&-~.&_--&-_-.&~-__.&-~.&~__~&-_~_.&~-~&-_~_.&~_--&~.&~~-.&~~-&@@&~_~&_--&~&_-~&-~~.&~.&-_~_.&~-~.&~_-_.&~--&~--.&-~~&~--.&-_~.&@@@@@@&_--&~~-.&@@@&_~~.&--&~~-.&~_-_&~-~&_~~.&~~-.&-_-&@@&@@&--~&_--&-~-&~_~.&~~-.&-~.&-~_.&@@@@@&-_~_.&-~_.&-~.&_--&~-&-~~.&~-__&_--~.&-_~_.&-~.&-~-&-~_.&~-&~-__.&-_.&@@@&~.&_-~.&~-~&~_-_.&~.&~-__.&_--~.&@@@@&-~~.&~--&~--.&~-__.&--~&~_~.&@@@@@@@@@@&-_.&_--~.&_-~.&-~.&_~~.&-_-.&~_-.&--~.&-_-&~_--.&~_-_&-~.&-~-.&-_.&-~.&--~.&~-_-&~_~&@&_--&-_&~_~&@@@&_~~.&@@@@&~.&-_~_.&~__~&~--&~-_-.&_~~&~--.&~_--&-_-.&~_-_&~--.&@&~-_-.&_--~&-~~&-_~.&~~-.&@&-~-&-_-&~__~.&-~.&~-_-.&-_~_&~_~&@@&-_-.&@@@@@@&~~-.&~--&~-__&@@@@@@@&~_~&~-~&~_--.&@&-_-.&_-~.&~-~&_--~.&-~~.&_~~&_--~.&-_&~-_-.&~_--.&-~-&--&--~&-_~.&@&~&~_-_.&_-~.&_--&@@@@@&~-&@@@@&~-__&_--&~__~.&-~_.&-~-&~_-&~~-.&~.&_~~.&~-~.&--~&-_-&@&@@@@@@&~--.&@&~_-&~__~.&~-__&~_--.&@@&-~.&~_~&-~.&--~.&-~.&-~_&~-~&~_~&~&~__~.&~.&-_~_.&_--.&~--.&@@@&-~-&-_~_&~-_-.&-~~.&-_-.&@&~_~&~.&-_-.&@@@@@@&~_~&@@@&-_-.&@@@&~_~&@&-_.&_-~.&-~~.&-_~_.&_~~.&~_-_&~-_-.&_~~&~--.&_~~&~~-.&-_-&~-~&_--&~~-.&_-~.&_--&@@@&~--.&-_-&_--~.&@@@@&--~&-_-&_~~.&--.&_--~.&~--&-~.&@@@&~~-.&_-~.&-_.&-~.&-_.&-~~.&~-_-.&-_&~_-_.&_--&@@@@@&@@&~_-_.&-_~.&-_.&~_-_.&~--.&@&@@&~-_-.&~-_-.&~-~&--~.&--&~__~.&~_--.&@@@@@@@@@@&~_-&~--.&-~.&-_.&~_-&~-&-~~.&~_-&_--~.&-~_&-~~.&~--.&~--&~_-_.&_~~&_~~.&~__~.&~-__&-_-&@&_-~&~_~&_-~.&_~~.&~-~&~_-_.&@@&~_-&~_--&-_~_.&@&_~~.&-_~_.&~-&-_~_.&-_.&~_--&_--~.&@@@@&~-__&--.&_--~.&~.&~_-_.&_--~&-_.&~_-.&~-__&~__~.&~.&-_~.&~--.&_--&~_~&~-~&~_-&@@@@@@&--~&-_~_.&~-~&@&~_-_.&-~_.&@@@@@@&~~-.&_--~.&-_-&-~.&~--&~-_-.&-_~_.&-_~_.&~-_-&_--.&@@@&--.&@@&~__~.&~_~.&~~-&_-~&~--.&-_~.&-_~_.&_--&~-__&~.&-_~_.&-~-.&~--.&-_-&-~.&-~-&-_-.&~_--.&_~~.&-_~.&~_~&~--&~--.&~-~.&-_~_.&@&-_~_.&~-_-&-~~.&~_-.&~-~&~__~&~--.&_-~.&--~.&_--&~--.&-_~_.&-~_.&@@@@@&~.&_--&-_.&~__~.&-_.&~_-.&-~.&~~-&-_-.&@@@@&~-~&--~.&-~~.&~--&-~.&~__~.&--~&-_~_.&-_~_.&@@@@&~_-_.&~_--.&~_-&~_~.&~-_-.&~_-_&-~.&_-~&_--~.&~_--.&-~.&~_--.&-_.&~--&-_~_.&~-~.&-_~_.&@@&-_.&_--~.&~-&@&@@@@@@@@@@&~-__.&_--~.&_-~.&~_-_.&_~~.&_--.&@&-~_.&~&--~&-_-&-_-.&@@@@@@&-_.&_-~.&~-~&~_-_.&-~~.&-~_.&@@&_--.&~_~&@@@@&-_.&~__~&-_~_.&-_~_.&_~~.&~-_-.&~.&~_--.&-_~_.&-_~.&-_.&~_-.&~_-&~_-.&~-__&~.&_~~.&~_-_.&-_.&~-__.&~_-&~--&-~_&-~~.&--.&@@@@@&~-&~_--.&-_.&~-_-.&~__~.&~_--.&~_-&@@@@@&_--~.&_--~&-~~&~&~.&-~.&_~~.&_--&-_~_.&@@@&~-~&~&-_~_.&-_~.&-_~_.&_~~.&~__~.&-~_.&~&@&-~~.&-_~.&_-~&~--&-_-.&_--~&@@@@@@@@@&@@@&~--.&~_-_&~_-&@@@&~_~&~_--.&@@@@@@@@@@&-~~.&~_~&~-~&~--.&@@@@@@@&-~~.&_--&@@@@@@@@@@&~--&~-&~.&@@@@@@@@@@&@@@&~.&-~_.&~_-&~_-_&~~-.&-_~.&~_-&~--.&~_--.&-~_.&@&@@@@@@&~_-_.&-_~.&~_-&~~-&~-_-.&@@@&_-~&~~-&~-&-~~.&~-__&~.&~-__&@@&-_.&~_--.&-_~_.&-_-&-~~&-~_.&~_~&-_-&~_-&~_-&-_~_.&~_--.&~_-&@@@@@@&_--.&@&-~-&~__~.&~-__&-~~.&-_.&-_&~.&~_--.&~_-_.&~.&~-_-.&-~~.&~--.&_~~&~-__&~_--.&-_-.&@&~_--.&_-~.&~-~&~__~&~~-.&~_-.&~--.&~-_-&-~_&~_-.&~_-_.&~-__.&-~_&~_-_&--.&~--&~__~.&~_-.&-_~&@@@@@@&-~~.&~.&-~.&_--~.&-_-.&_~~&~--.&~-~&~_--.&_~~&~_-_.&~--&-~_&_~~&-~.&_-~.&~-__&~_-.&-_~_.&_--~&~-&_-~.&-~-&_--.&~~-.&@@@@&~_~&@&~_--.&~_-_&-_.&~__~.&~-_-.&~.&-_~&@&-_.&~--&~-~&~_-.&~--.&~--&_~~.&@@@@&-~~.&-~.&-_~_.&--.&~_-_.&--&@@@@@@@@@@&-~_.&--~&@&~&~--&~_-_.&-~~.&-_.&~--.&~-_-.&_-~.&-~.&~--&_--~.&-_-&~-~&~--&-~~.&--&@@@@@@@@@@&~-~&~~-.&_--&@@@@@@&~-__.&~-__&--&@@@@@@@@@@&~_-&-_.&~.&-~~&@@@@&~-_-.&@&@@&~_-&~__~.&-~_.&@@@@@@&-~-.&~--.&-~~.&-_.&-_-.&-~~.&-_~.&-~-&_~~.&-_-.&_~~&~_-_.&--.&-_.&~_-_&~_-&@@@@@@&~_~&-~.&~-.&@@@&~_--.&@@@&@&@@@@@@&~_--.&_--&-~_.&@@@@@@&--~&_-~.&-~-&@@@@@@@&-_~_.&~--&~-~&~__~&-_~_.&~-~&-~_.&@@@@@@&~_~&_--&@@@@@@&~--.&--~&_~~&~--.&-~_.&--~&_-~.&~-~&~_~.&-~~.&-~.&-_~_.&~&~-&_--&-~-&-_~_&~-&@@@&~-__&~__~.&-_~_.&-_-&-_~_.&@@@@@@@&-_.&-~.&-~.&~_-&-_-.&-~.&--.&@@@@@&~-_-.&_--~&@@@@@@@@@@&@@@@@@@&-_.&-~.&~-__&@@@@@@&-_~_.&~-__.&_~~.&-~.&-~_&--&@@@@@@@@@@&@@@@@&_--.&~--&~-.&@&-_-.&-_~_.&~-__&~__~&--~&@@@@&--~.&-_-&-_.&_--&~&~--&-_~_.&-~.&-_~_.&-~_.&~-__&_--~&~~-&_--~&-_-.&_--&-_.&~_--&~__~.&~-__.&--~.&_-~&~~-.&-_~.&~_-&~-&-~~.&~-~&-_~&@@@@&_--.&@@@&~--.&@&~_~&@@@&@@@@@&@@@@@&~_~&-_~.&~_-&-_&_--~.&~.&@@@@@@@@@@&~_-.&~--.&@@@&@@&~__~&~_~&-_~.&-_.&-~-.&~_-_.&~-__.&~_-_.&@@@@@&~-&~_--.&-~~&-~~.&~.&~_--.&--.&@@@&-_.&~_--.&~--.&@@@@&~-~.&@@@@&-_~_.&--~.&~_--.&@@&~-__&@@@&~-~.&@&@@@@@@@@@@&@@@@&~_--.&-_-&-~-&@@@&--~&~-__.&_--&@@@@@&~-&~-__.&~-~&_--.&~--.&~_--.&~_-&~.&~~-.&-_~_.&_~~.&~_-_.&--~&_-~.&_--&@@@@@&~_-_.&~_--.&_~~.&~_~.&-~_&_--&~--.&~.&-_~_.&-~~.&~--.&_~~.&-~~.&~_-_&-_.&_--&~-&~.&_~~.&~-~&~~-.&@@@&-_.&~_--&~_--.&-~~.&~_-&~.&~__~.&~_--.&~_-&@@@@&~~-.&~.&~-__&@@@&_--~.&_-~.&-~.&@@@&~-~.&@@&-_.&~-&-_-.&~.&~_-_.&-~-&~-_-.&_-~.&~_-_.&~.&-~~.&_--~&-~~&_-~.&~.&_--&~-~&@@@@&~-&-~.&~_-&-_-.&~-~.&~--&-_-.&_--~&-~~.&~.&-~~&~-~&-_.&~-~&~_-_.&~_--.&~__~.&-_~_.&~_~&~--&~-_-.&_--&-~-&~-~.&~-__&~-~&~_-_.&-_~_&-_~_.&~_-.&~_-_.&--&~-_-.&~.&~-__&~--.&~--.&@@@&~_~&_--~&-~_&~_--.&@@@@@@&_--&~__~.&-~_.&@@@@@@@@@&-_~_&-~_&~.&~-__&~_--.&~-__&~-__.&~_~&-_&-_.&@&~--.&_-~.&~_-_.&-~_.&@@@@@&-_&~_~&~_-.&-~-&~_--&-~~.&_~~&~_-_.&@@@@@@&-_-.&_--&-~~&-_.&~-_-.&@@&-_~_.&~&~_--.&@@@&@@@@@&@@@@@&~_~&~--&~--.&-_~.&-~~.&_--&~&-_~_&--~&@&~_~&-_&~-_-.&_-~.&~-.&~&~~-.&@&-~.&_~~.&-~_&-_~_.&~_-&-~_.&~__~.&-~_.&@@&~_--&~~-.&-~.&-_.&@@@&~~-.&~-~&~__~&_-~&-_~_.&@@@&~-~&-~-.&-~_&_--&-_.&@&~--.&-_-&@@@@@@&~&-_.&@@@@&~-~&-_~.&~-__&-_~&__~~&__~~
""".trimIndent()
        val dataKey = "&~-__&@@@&~-~&~-~&~--.&-_-&-_~&@@@@@@&~_~&~_--.&_~~.&-_~.&-_.&_--&~-~&-~.&~_--" +
                ".&~_--.&~_-&-_~.&-_.&@@&-~.&-~.&-_.&_--&@@@@@@@@@@&-_~_.&~_--.&~_--" +
                ".&~-__&-_~.&-_~" +
                "_.&~-~&-~.&~_-.&-_~_.&_--&~-__&-_.&-_.&-~~.&~_~&__~~"
        return when(key){
            1->{
                dataKey
            }

            2->{
                dbAuth
            }

            else->{
                dataKey
            }
        }
    }
    /**
     * Encodes the input string using a custom encoding scheme after converting it to Base64.
     *
     * @param inputStr The string to be encoded.
     * @return The encoded string using the custom encoding scheme.
     */
    fun encodeData(inputStr: String): String {
        val charMap = mapOf(
            'a' to "&~-", 'b' to "&--~", 'c' to "&~_~", 'd' to "&~-__", 'e' to "&-~_",
            'f' to "&-~", 'g' to "&-_~", 'h' to "&~_-", 'i' to "&--", 'j' to "&_~~",
            'k' to "&_--", 'l' to "&~-~", 'm' to "&-_-", 'n' to "&~_-_", 'o' to "&~__~",
            'p' to "&-~-", 'q' to "&~-_-", 'r' to "&-_", 's' to "&~~-", 't' to "&-~~",
            'u' to "&~_--", 'v' to "&-_~_", 'w' to "&~", 'x' to "&_-~", 'y' to "&_--~",
            'z' to "&~--", '=' to "&__~~", ' ' to "&__", 'A' to "&~-.", 'B' to "&--~.",
            'C' to "&~_~.", 'D' to "&~-__.", 'E' to "&-~_.",
            'F' to "&-~.", 'G' to "&-_~.", 'H' to "&~_-.", 'I' to "&--.", 'J' to "&_~~.",
            'K' to "&_--.", 'L' to "&~-~.", 'M' to "&-_-.", 'N' to "&~_-_.", 'O' to "&~__~.",
            'P' to "&-~-.", 'Q' to "&~-_-.", 'R' to "&-_.", 'S' to "&~~-.", 'T' to "&-~~.",
            'U' to "&~_--.", 'V' to "&-_~_.", 'W' to "&~.", 'X' to "&_-~.", 'Y' to "&_--~.",
            'Z' to "&~--.", '0' to "&@", '1' to "&@@", '2' to "&@@@", '3' to "&@@@@",
            '4' to "&@@@@@", '5' to "&@@@@@@",
            '6' to "&@@@@@@@",
            '7' to "&@@@@@@@@", '8' to "&@@@@@@@@@", '9' to "&@@@@@@@@@@"
        )
        val byteArray = inputStr.toByteArray(Charsets.UTF_8)
        val base = Base64.getEncoder().encodeToString(byteArray)
        return base.map { charMap[it] ?: it }.joinToString(separator = "")
    }

    /**
     * Decodes the Base64 encoded string back to its original form.
     *
     * @param str The Base64 encoded string to be decoded.
     * @return The decoded string in its original form.
     */
    fun deBase(str: String): String {
        val decodedBytes = Base64.getDecoder().decode(str)
        return String(decodedBytes, Charsets.UTF_8)
    }
    /**
     * Decodes the input string encoded with the custom encoding scheme back to its Base64 form.
     *
     * @param inputStr The custom-encoded string to be decoded.
     * @return The decoded string in its Base64 form.
     */
    fun decodeData(inputStr: String): String {
        val charMap = mapOf(
            "&~-" to 'a', "&--~" to 'b', "&~_~" to 'c', "&~-__" to 'd', "&-~_" to 'e',
            "&-~" to 'f', "&-_~" to 'g', "&~_-" to 'h', "&--" to 'i', "&_~~" to 'j',
            "&_--" to 'k', "&~-~" to 'l', "&-_-" to 'm', "&~_-_" to 'n', "&~__~" to 'o',
            "&-~-" to 'p', "&~-_-" to 'q', "&-_" to 'r', "&~~-" to 's', "&-~~" to 't',
            "&~_--" to 'u', "&-_~_" to 'v', "&~" to 'w', "&_-~" to 'x', "&_--~" to 'y',
            "&~--" to 'z', "&__~~" to '=', "&__" to ' ', "&~-." to 'A', "&--~." to 'B',
            "&~_~." to 'C', "&~-__." to 'D', "&-~_." to 'E',
            "&-~." to 'F', "&-_~." to 'G', "&~_-." to 'H', "&--." to 'I', "&_~~." to 'J',
            "&_--." to 'K', "&~-~." to 'L', "&-_-." to 'M', "&~_-_." to 'N', "&~__~." to 'O',
            "&-~-." to 'P', "&~-_-." to 'Q', "&-_." to 'R', "&~~-." to 'S', "&-~~." to 'T',
            "&~_--." to 'U', "&-_~_." to 'V', "&~." to 'W', "&_-~." to 'X', "&_--~." to 'Y',
            "&~--." to 'Z', "&@" to '0', "&@@" to '1', "&@@@" to '2', "&@@@@" to '3', "&@@@@@" to '4',
            "&@@@@@@" to '5', "&@@@@@@@" to '6', "&@@@@@@@@" to '7', "&@@@@@@@@@" to '8', "&@@@@@@@@@@" to '9'
        )

        val outputStr = StringBuilder()
        val inputStrArr = inputStr.split("&")
        for (i in inputStrArr) {
            val key = "&$i"
            if (key == "&") {
                continue
            }
            outputStr.append(charMap[key])
        }
        return outputStr.toString()
    }
    fun compressText(inputText: String): String {
        val inputBytes = inputText.toByteArray()
        val outputStream = ByteArrayOutputStream()
        val compressorOutputStream = CompressorStreamFactory().createCompressorOutputStream(
            CompressorStreamFactory.GZIP, outputStream
        )
        compressorOutputStream.write(inputBytes)
        compressorOutputStream.close()
        outputStream.close()

        val compressedBytes = outputStream.toByteArray()
        return Base64.getEncoder().encodeToString(compressedBytes)
    }
    fun decompressText(compressedText: String): String {
        val compressedBytes = Base64.getDecoder().decode(compressedText)
        val inputStream = compressedBytes.inputStream()
        val decompressorInputStream = CompressorStreamFactory().createCompressorInputStream(
            CompressorStreamFactory.GZIP, inputStream
        )

        val outputBytes = decompressorInputStream.readBytes()
        decompressorInputStream.close()
        return outputBytes.toString(Charsets.UTF_8)
    }
}

fun String.toEncodedString(): String {
    val charMap = mapOf(
        'a' to "&~-", 'b' to "&--~", 'c' to "&~_~", 'd' to "&~-__", 'e' to "&-~_",
        'f' to "&-~", 'g' to "&-_~", 'h' to "&~_-", 'i' to "&--", 'j' to "&_~~",
        'k' to "&_--", 'l' to "&~-~", 'm' to "&-_-", 'n' to "&~_-_", 'o' to "&~__~",
        'p' to "&-~-", 'q' to "&~-_-", 'r' to "&-_", 's' to "&~~-", 't' to "&-~~",
        'u' to "&~_--", 'v' to "&-_~_", 'w' to "&~", 'x' to "&_-~", 'y' to "&_--~",
        'z' to "&~--", '=' to "&__~~", ' ' to "&__", 'A' to "&~-.", 'B' to "&--~.",
        'C' to "&~_~.", 'D' to "&~-__.", 'E' to "&-~_.",
        'F' to "&-~.", 'G' to "&-_~.", 'H' to "&~_-.", 'I' to "&--.", 'J' to "&_~~.",
        'K' to "&_--.", 'L' to "&~-~.", 'M' to "&-_-.", 'N' to "&~_-_.", 'O' to "&~__~.",
        'P' to "&-~-.", 'Q' to "&~-_-.", 'R' to "&-_.", 'S' to "&~~-.", 'T' to "&-~~.",
        'U' to "&~_--.", 'V' to "&-_~_.", 'W' to "&~.", 'X' to "&_-~.", 'Y' to "&_--~.",
        'Z' to "&~--.", '0' to "&@", '1' to "&@@", '2' to "&@@@", '3' to "&@@@@",
        '4' to "&@@@@@", '5' to "&@@@@@@",
        '6' to "&@@@@@@@",
        '7' to "&@@@@@@@@", '8' to "&@@@@@@@@@", '9' to "&@@@@@@@@@@"
    )
    val byteArray = this.toByteArray(Charsets.UTF_8)
    val base = Base64.getEncoder().encodeToString(byteArray)
    return base.map { charMap[it] ?: it }.joinToString(separator = "")
}

fun String.toDecodedString(): String {
    val charMap = mapOf(
        "&~-" to 'a', "&--~" to 'b', "&~_~" to 'c', "&~-__" to 'd', "&-~_" to 'e',
        "&-~" to 'f', "&-_~" to 'g', "&~_-" to 'h', "&--" to 'i', "&_~~" to 'j',
        "&_--" to 'k', "&~-~" to 'l', "&-_-" to 'm', "&~_-_" to 'n', "&~__~" to 'o',
        "&-~-" to 'p', "&~-_-" to 'q', "&-_" to 'r', "&~~-" to 's', "&-~~" to 't',
        "&~_--" to 'u', "&-_~_" to 'v', "&~" to 'w', "&_-~" to 'x', "&_--~" to 'y',
        "&~--" to 'z', "&__~~" to '=', "&__" to ' ', "&~-." to 'A', "&--~." to 'B',
        "&~_~." to 'C', "&~-__." to 'D', "&-~_." to 'E',
        "&-~." to 'F', "&-_~." to 'G', "&~_-." to 'H', "&--." to 'I', "&_~~." to 'J',
        "&_--." to 'K', "&~-~." to 'L', "&-_-." to 'M', "&~_-_." to 'N', "&~__~." to 'O',
        "&-~-." to 'P', "&~-_-." to 'Q', "&-_." to 'R', "&~~-." to 'S', "&-~~." to 'T',
        "&~_--." to 'U', "&-_~_." to 'V', "&~." to 'W', "&_-~." to 'X', "&_--~." to 'Y',
        "&~--." to 'Z', "&@" to '0', "&@@" to '1', "&@@@" to '2', "&@@@@" to '3', "&@@@@@" to '4',
        "&@@@@@@" to '5', "&@@@@@@@" to '6', "&@@@@@@@@" to '7', "&@@@@@@@@@" to '8', "&@@@@@@@@@@" to '9'
    )

    val outputStr = StringBuilder()
    val inputStrArr = this.split("&")
    for (i in inputStrArr) {
        val key = "&$i"
        if (key == "&") {
            continue
        }
        outputStr.append(charMap[key])
    }
    fun unBase(str: String): String {
        val decodedBytes = Base64.getDecoder().decode(str)
        return String(decodedBytes, Charsets.UTF_8)
    }
    return unBase(outputStr.toString())

}


// Extension function to encrypt a string using AES encryption
@SuppressLint("GetInstance")
fun String.encryptAes(secretKey: String): String {
    val key: Key = SecretKeySpec(secretKey.toByteArray(), "AES")
    val cipher: Cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.ENCRYPT_MODE, key)
    val encryptedBytes: ByteArray = cipher.doFinal(this.toByteArray())
    return Base64.getEncoder().encodeToString(encryptedBytes)
}



// Extension function to decrypt an AES encrypted string
@SuppressLint("GetInstance")
fun String.decryptAes(secretKey: String): String {
    val key: Key = SecretKeySpec(secretKey.toByteArray(), "AES")
    val cipher: Cipher = Cipher.getInstance("AES")
    cipher.init(Cipher.DECRYPT_MODE, key)
    val decryptedBytes: ByteArray = cipher.doFinal(Base64.getDecoder().decode(this))
    return String(decryptedBytes)
}

fun String.fullDecode():String{
    return this.toDecodedString().decryptAes(DataEncode().getKeys(1).toDecodedString())
}
fun String.fullEncode():String{
    return this.encryptAes(DataEncode().getKeys(1).toDecodedString()).toEncodedString()
}


fun String.decompressString(): String {
    val compressedBytes = Base64.getDecoder().decode(this)
    val inputStream = compressedBytes.inputStream()
    val decompressorInputStream = CompressorStreamFactory().createCompressorInputStream(
        CompressorStreamFactory.GZIP, inputStream
    )

    val outputBytes = decompressorInputStream.readBytes()
    decompressorInputStream.close()
    return outputBytes.toString(Charsets.UTF_8)
}
fun String.compressString(): String {
    val inputBytes = this.toByteArray()
    val outputStream = ByteArrayOutputStream()
    val compressorOutputStream = CompressorStreamFactory().createCompressorOutputStream(
        CompressorStreamFactory.GZIP, outputStream
    )
    compressorOutputStream.write(inputBytes)
    compressorOutputStream.close()
    outputStream.close()

    val compressedBytes = outputStream.toByteArray()
    return Base64.getEncoder().encodeToString(compressedBytes)
}
fun String.shorten():List<String>{
    var thisEndPosition = 1
    var thisPosition = 0
    val stringMap = mutableListOf<String>()
    var currentChar = ""

    for (i in this){
        if(thisPosition == 0) {
            thisPosition++
            currentChar = i.toString()
            continue
        }

        if (i.toString() == currentChar){
            thisEndPosition++
        }else{
            val thisPositionMap = "$thisPosition:$thisEndPosition=$currentChar"
            stringMap.add(thisPositionMap)
            currentChar = i.toString()
            thisPosition = thisEndPosition+1
            thisEndPosition++
        }
        if(this.length == thisEndPosition){
            val thisPositionMap = "$thisPosition:$thisEndPosition=${i}"
            stringMap.add(thisPositionMap)
            break
        }
    }
    return stringMap
}

fun List<String>.rebuildString(): String {
    val stringBuilder = StringBuilder()

    for (segment in this) {
        val parts = segment.split(":")
        val startPosition = parts[0].toInt()
        val endPosition = parts[1].substringBefore("=").toInt()
        val repeatedChar = parts[1].substringAfter("=")

        for (i in startPosition..endPosition) {
            stringBuilder.append(repeatedChar)
        }
    }

    return stringBuilder.toString()
}






















