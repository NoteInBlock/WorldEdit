/*
 * WorldEdit, a Minecraft world manipulation toolkit
 * Copyright (C) sk89q <http://www.sk89q.com>
 * Copyright (C) WorldEdit team and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.sk89q.worldedit.util.asset.holder;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class ImageHeightmap {

    private final BufferedImage image;

    private BufferedImage resizedImage;
    private int lastSize = -1;

    public ImageHeightmap(BufferedImage image) {
        this.image = image;
    }

    public double getHeightAt(int x, int y, int size, double intensity) {
        if (size != lastSize || resizedImage == null) {
            int diameter = size * 2 + 1;
            resizedImage = new BufferedImage(diameter, diameter, 1);
            Graphics2D graphic = null;
            try {
                graphic = resizedImage.createGraphics();
                graphic.drawImage(this.image, 0, 0, diameter, diameter, null);
            } finally {
                if (graphic != null) {
                    graphic.dispose();
                }
            }
            lastSize = size;
        }

        // Flip the Y axis
        y = resizedImage.getHeight() - 1 - y;

        int rgb = resizedImage.getRGB(x, y);
        if (rgb == 0) {
            return 0;
        }

        int red = rgb >>> 16 & 0xFF;
        int green = rgb >>> 8 & 0xFF;
        int blue = rgb & 0xFF;

        double scale = (red + blue + green) / 3D / 255D;
        return scale * intensity;
    }
}
