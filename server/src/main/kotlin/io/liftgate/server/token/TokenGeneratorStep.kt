package io.liftgate.server.token

import io.liftgate.server.LiftgateEngine
import io.liftgate.server.logger
import io.liftgate.server.startup.StartupStep
import java.io.File
import java.nio.file.Files

/**
 * @author GrowlyX
 * @since 8/15/2022
 */
object TokenGeneratorStep : StartupStep
{
    override fun perform(context: LiftgateEngine)
    {
        val tokenPath = File("token.txt")

        if (!tokenPath.exists())
        {
            logger.info("[Token] Generating new client -> server authorization token at: [token.txt]")

            val generated = TokenGenerator.generate()

            val logInfo = """
                # This token will be used to authenticate client -> server RPCs. Do not share this token.
                # A new token may be regenerated by simply deleting this file.
                $generated
            """.trimIndent()

            tokenPath.createNewFile()
            tokenPath.writeText(logInfo)

            logger.info(
                "[Token] Generated new token.\n$logInfo"
            )
        }

        val decoded = Files
            .readAllBytes(tokenPath.toPath())
            .decodeToString()

        TokenGenerator.cached = decoded
    }
}
