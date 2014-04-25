package emd24.rpgmod.combatitems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderEntity;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class RenderHolyHandGrenade extends RenderSnowball {

	    public RenderHolyHandGrenade(Item par1Item) {
		super(par1Item);
		// TODO Auto-generated constructor stub
	}


		public void renderHolyHandGrenade(HolyHandGrenadeEntity ent, double par2, double par4, double par6, float par8, float par9)
	    {
	        super.doRender(ent, par2, par4, par6, par8, par9);
	    }


	    /**
	     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	     * (Render<T extends Entity) and this method has signature public void doRender(T entity, double d, double d1,
	     * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	     */
	    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
	    {
	        this.renderHolyHandGrenade((HolyHandGrenadeEntity) par1Entity, par2, par4, par6, par8, par9);
	    }

}
