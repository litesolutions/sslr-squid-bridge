/*
 * SSLR Squid Bridge
 * Copyright (C) 2010-2022 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.squidbridge.recognizer;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

public class RegexDetectorTest {

  @Test()
  public void testNegativeProbability() {
    assertThrows(IllegalArgumentException.class, () -> {
      new RegexDetector("toto", -1);
    });
  }

  @Test
  public void testProbabilityHigherThan1() {
    assertThrows(IllegalArgumentException.class, () -> {
      new RegexDetector("toto", 1.2);
    });
  }

  @Test
  public void testProbability() {
    RegexDetector pattern = new RegexDetector("toto", 0.3);
    assertEquals(0.3, pattern.recognition(" toto "), 0.01);
    assertEquals(0, pattern.recognition("sql"), 0.01);
    assertEquals(1 - Math.pow(0.7, 3), pattern.recognition(" toto toto toto "), 0.01);
  }

  @Test
  public void testSeveralMatches() {
    RegexDetector pattern = new RegexDetector("(\\S\\.\\S)", 0.3); // \S is non-whitespace character
    assertEquals(0.0, pattern.recognition(" toto "), 0.001);
    assertEquals(0.3, pattern.recognition("abc.def ghi jkl"), 0.001);
    assertEquals(0.51, pattern.recognition("abc.def.ghi"), 0.001);
    assertEquals(0.51, pattern.recognition("abc.def ghi.jkl"), 0.001);
  }
}
