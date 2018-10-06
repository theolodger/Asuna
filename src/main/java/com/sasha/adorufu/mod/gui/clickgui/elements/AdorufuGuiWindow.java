/*
 * Copyright (c) Sasha Stevens 2018.
 *
 * This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.sasha.adorufu.mod.gui.clickgui.elements;

import com.sasha.adorufu.mod.AdorufuMod;
import com.sasha.adorufu.mod.misc.AdorufuMath;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AdorufuGuiWindow implements IAdorufuGuiElement {

    private String title;
    private float themeColourR = 255f;
    private float themeColourG = 255f;
    private float themeColourB = 255f;
    private float themeColourA = 5f;
    private List<IAdorufuGuiElement> moduleElements = new ArrayList<>();
    private int x;
    private int length;
    private int y;
    private int width;

    public AdorufuGuiWindow(int x, int y, int length, int width, String title, List<IAdorufuGuiElement> moduleElements) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.title = title;
        this.moduleElements = moduleElements;
    }
    public AdorufuGuiWindow(int x, int y, int length, int width, int colour, String title, List<IAdorufuGuiElement> moduleElements) {
        this.x = x;
        this.y = y;
        this.title = title;
        this.length = length;
        this.width = width;
        themeColourA = (float) (colour >> 24 & 0xFF) / 255F;
        themeColourR = (float) (colour >> 16 & 0xFF) / 255F;
        themeColourG = (float) (colour >> 8 & 0xFF) / 255F;
        themeColourB = (float) (colour & 0xFF) / 255F;
        this.moduleElements = moduleElements;
    }
    public AdorufuGuiWindow(int x, int y, int length, int width, float colourR, float colourG, float colourB, float colourA, String title, List<IAdorufuGuiElement> moduleElements) {
        this.x = x;
        this.y = y;
        this.length = length;
        this.width = width;
        this.title = title;
        themeColourA = colourA;
        themeColourR = colourR;
        themeColourG = colourG;
        themeColourB = colourB;
        this.moduleElements = moduleElements;
    }

    private String getTitle() {
        return this.title;
    }

    @Override
    public void drawElement() {
        GL11.glPushMatrix();
        GL11.glPushAttrib(8256);
        drawTitlebar();
        drawRestOfWindow();
        GL11.glPopMatrix();
        GL11.glPopAttrib();
    }

    private void drawTitlebar() {
        AdorufuMath.drawRect(this.x, this.y,
                this.x + this.width,
                this.y + 20,
                themeColourR, themeColourG, themeColourB, themeColourA);
        AdorufuMod.FONT_MANAGER.segoe_36.drawCenteredString(this.title, (this.x + (this.width / 2)), this.y - 10, 0xffffff, true);
    }
    private void drawRestOfWindow() {
        AdorufuMath.drawRect(this.x, (this.y + 20), this.x + this.width, (this.y + 20) + this.length, Integer.MIN_VALUE);
        AtomicInteger c = new AtomicInteger();
        this.moduleElements.forEach(button -> {
            button.setX(this.x);
            button.setY(this.y + (15 * c.get()));
            c.getAndIncrement();
        });
    }

    @Override
    public void onMouseEngage(int x, int y, int b) {

    }

    @Override
    public void onMouseRelease(int x, int y, int b) {

    }


    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
    public void setY(int y) {
        this.y = y;
    }

}