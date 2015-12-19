/**
 * SPDXVersion: SPDX-1.1
 * Creator: Person: Mikhail Vorontsov
 * Created: 2013-05-18T00:00:00Z
 * LicenseName: Freeware
 * FileCopyrightText: <text> Copyright (c) Mikhail Vorontsov</text>
 * FileComment: <text> A model to read a large text file from disk, processing
 *  each line at maximum speed.
 * Original code: http://java-performance.info/java-io-bufferedinputstream-and-java-util-zip-gzipinputstream/
 * </text> 
 */
package utils.ReadWrite;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author nunobrito
 */
public class FasterBufferedInputStream extends BufferedInputStream
{
    public FasterBufferedInputStream(InputStream in, int size) {
        super(in, size);
    }
 
    //This method returns positive value if something is available, otherwise it will return zero.
    @Override
    public int available() throws IOException {
        if (in == null)
            throw new IOException( "Stream closed" );
        final int n = count - pos;
        return n > 0 ? n : in.available();
    }
}