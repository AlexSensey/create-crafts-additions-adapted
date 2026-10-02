package createadditionclientprobe;
import java.nio.file.*;
import net.minecraft.client.Minecraft;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import com.mrh0.createaddition.index.CAPartials;
import com.mrh0.createaddition.rendering.CARenderType;
@Mod("create_world_log_probe")
public class CreateAdditionClientProbe {
 int ticks,index,frame;boolean done,validated;String result;java.util.List<mezz.jei.api.recipe.category.IRecipeCategory<?>> categories;
 public CreateAdditionClientProbe(){NeoForge.EVENT_BUS.addListener(this::tick);}
 void tick(ClientTickEvent.Post e){
  var mc=Minecraft.getInstance();
  if(mc.gui.screen() instanceof net.minecraft.client.gui.screens.BackupConfirmScreen screen){try{var f=screen.getClass().getDeclaredField("onProceed");f.setAccessible(true);((net.minecraft.client.gui.screens.BackupConfirmScreen.Listener)f.get(screen)).proceed(true,false);}catch(Exception err){throw new RuntimeException(err);}return;}
  if(done||mc.level==null||mc.player==null||mc.gui.overlay()!=null||++ticks<120)return;
  if(com.simibubi.create.compat.jei.CreateJEI.runtime==null)return;
  try {
   var runtime=com.simibubi.create.compat.jei.CreateJEI.runtime;
   if(validated){
    if(index==categories.size()){
     Files.writeString(mc.gameDirectory.toPath().resolve("WORLD-LOADED.txt"),result+"PASS all three Crafts & Additions JEI category screens rendered.\n");done=true;mc.stop();return;
    }
    if(frame==0)runtime.getRecipesGui().showTypes(java.util.List.of(categories.get(index).getRecipeType()));
    if(++frame==35){index++;frame=0;}return;
   }
   int models=0;
   for(var field:CAPartials.class.getFields()){
    if(field.getType()!=dev.engine_room.flywheel.lib.model.baked.PartialModel.class)continue;
    var model=((dev.engine_room.flywheel.lib.model.baked.PartialModel)field.get(null)).get();
    if(model==null)throw new AssertionError("Unbaked partial model: "+field.getName());
    var parts=new java.util.ArrayList<net.minecraft.client.renderer.block.dispatch.BlockStateModelPart>();
    model.collectParts(net.minecraft.util.RandomSource.create(0),parts);
    if(parts.isEmpty())throw new AssertionError("Empty partial model: "+field.getName());
    models++;
   }
   var f=CARenderType.class.getDeclaredField("WIRE_PIPELINE");f.setAccessible(true);
   var pipeline=(com.mojang.blaze3d.pipeline.RenderPipeline)f.get(null);
   if(pipeline.getVertexFormatMode()!=com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS)throw new AssertionError("Wire topology changed");
   if(models!=7)throw new AssertionError("Unexpected partial model count");
   result="PASS Crafts & Additions 26.1.2 client world loaded; all 7 partial models baked and wire pipeline retained QUADS topology.\n";
   var manager=runtime.getRecipeManager();
   categories=manager.createRecipeCategoryLookup().includeHidden().get().filter(c->c.getRecipeType().getUid().getNamespace().equals("createaddition")).toList();
   if(categories.size()!=3)throw new AssertionError("Missing Crafts & Additions JEI categories: "+categories.size());
   var focus=runtime.getJeiHelpers().getFocusFactory().getEmptyFocusGroup();int layouts=0;
   for(mezz.jei.api.recipe.category.IRecipeCategory category:categories){
    var recipes=manager.createRecipeLookup(category.getRecipeType()).includeHidden().get().toList();
    if(recipes.isEmpty())throw new AssertionError("Empty JEI category: "+category.getRecipeType().getUid());
    for(Object recipe:recipes){
     var layout=(mezz.jei.api.gui.IRecipeLayoutDrawable<?>)manager.createRecipeLayoutDrawable(category,recipe,focus).orElseThrow();
     var graphics=new net.minecraft.client.gui.GuiGraphicsExtractor(mc,new net.minecraft.client.renderer.state.gui.GuiRenderState(),0,0);
     layout.drawRecipe(graphics,-1000,-1000);layouts++;
    }
    result+="PASS JEI "+category.getRecipeType().getUid()+": "+recipes.size()+" recipes\n";
   }
   result+="PASS recipe layout and animated preview extraction: "+layouts+" layouts\n";
   validated=true;return;
  }catch(Throwable err){err.printStackTrace();try{Files.writeString(mc.gameDirectory.toPath().resolve("CREATEADDITION-CLIENT-FAIL.txt"),err.toString());}catch(Exception ignored){}}
  done=true;mc.stop();
 }
}
