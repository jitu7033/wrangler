/*
 *  Copyright © 2017-2019 Cask Data, Inc.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not
 *  use this file except in compliance with the License. You may obtain a copy of
 *  the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations under
 *  the License.
 */

public class ByteSizeTest {

    /**
     * Tests parsing of standard byte size units (KB, MB, GB) with expected values.
     */
    @Test
    public void testByteSizeParsing() {
        Assert.assertEquals(1024, ByteSize.parse("1KB").getValue());
        Assert.assertEquals(1536, ByteSize.parse("1.5KB").getValue());
        Assert.assertEquals(1048576, ByteSize.parse("1MB").getValue());
        Assert.assertEquals(1073741824, ByteSize.parse("1GB").getValue());
    }

    /**
     * Tests parsing of lowercase input to ensure case-insensitive parsing works correctly.
     */
    @Test
    public void testByteSizeParsingLowerCase() {
        Assert.assertEquals(2048, ByteSize.parse("2kb").getValue());
    }

    /**
     * Tests handling of invalid input. The string "10ZZ" is not a valid unit and should throw an exception.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidByteSize() {
        ByteSize.parse("10ZZ");
    }
}
