package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.generation.GenerationProfile;
import groovy.lang.GroovyShell;
import groovy.lang.Script;
import net.minecraft.resources.ResourceLocation;
import org.codehaus.groovy.control.CompilerConfiguration;

import java.io.*;
import java.util.function.Function;

public class GenerationProfileLoader {
	public static final String PROFILE_FILE_EXTENSION = ".lrg";

	private final CompilerConfiguration compilerConfiguration;

	public GenerationProfileLoader()
	{
		this.compilerConfiguration = new CompilerConfiguration();
		this.compilerConfiguration.setScriptBaseClass(GrammarConfigurationScript.class.getCanonicalName());
	}

	public GroovyShell createShell()
	{
		return new GroovyShell(this.getClass().getClassLoader(), this.compilerConfiguration);
	}

	public Reader openScript(ResourceLocation scriptKey) throws IOException
	{
		return new FileReader(scriptKey.getPath() + PROFILE_FILE_EXTENSION);
	}

	public Script loadScript(ResourceLocation scriptKey) throws IOException
	{
		try(Reader scriptReader = this.openScript(scriptKey))
		{
			GroovyShell shell = this.createShell();
			File file = new File(scriptKey.getPath());
			return shell.parse(scriptReader, file.getName());
		}
	}

	public GenerationProfile loadProfile(ResourceLocation scriptKey) throws IOException
	{
		Script script = this.loadScript(scriptKey);
		return (GenerationProfile)script.run();
	}

	public Function<ResourceLocation, GenerationProfile> loader()
	{
		return (resourceLocation) -> {
			try
			{
				return loadProfile(resourceLocation);
			}
			catch(IOException exc)
			{
				throw new RuntimeException(exc);
			}
		};
	}

	// A testing entrypoint
	public static void main(String[] args) throws Exception
	{
		GenerationProfileLoader loader = new GenerationProfileLoader();
		GenerationProfile profile = loader.loadProfile(ResourceLocation.fromNamespaceAndPath(LivelyRealmsMod.MODID, "sample"));
		System.out.println(profile);
	}
}
