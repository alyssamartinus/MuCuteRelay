package com.mucheng.mucute.relay.util

import com.google.gson.JsonParser
import com.mucheng.mucute.relay.MuCuteRelay
import com.mucheng.mucute.relay.MuCuteRelaySession
import com.mucheng.mucute.relay.address.MuCuteAddress
import net.lenni0451.commons.httpclient.RetryHandler
import net.raphimc.minecraftauth.MinecraftAuth
import net.raphimc.minecraftauth.step.bedrock.session.StepFullBedrockSession
import net.raphimc.minecraftauth.step.msa.StepMsaDeviceCode.MsaDeviceCodeCallback
import org.cloudburstmc.protocol.bedrock.BedrockPong
import java.io.File
import java.nio.file.Paths

fun captureGamePacket(
    advertisement: BedrockPong = MuCuteRelay.DefaultAdvertisement,
    localAddress: MuCuteAddress = MuCuteAddress("0.0.0.0", 19132),
    remoteAddress: MuCuteAddress,
    onSessionCreated: MuCuteRelaySession.() -> Unit
): MuCuteRelay {
    return MuCuteRelay(
        localAddress = localAddress,
        advertisement = advertisement
    ).capture(
        remoteAddress = remoteAddress,
        onSessionCreated = onSessionCreated
    )
}

fun authorize(
    cache: Boolean = true,
    file: File? = Paths.get(".").resolve("bedrockSession.json").toFile(),
    msaDeviceCodeCallback: MsaDeviceCodeCallback = MsaDeviceCodeCallback {
        println("Go to ${it.directVerificationUri}")
    }
): StepFullBedrockSession.FullBedrockSession {
    if (cache && file != null && file.exists()) {
        val json = JsonParser.parseString(file.readText()).asJsonObject
        return MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN.fromJson(json)
    }

    val httpClient = MinecraftAuth.createHttpClient()
    httpClient.connectTimeout = 30000
    httpClient.readTimeout = 30000
    httpClient.setRetryHandler(RetryHandler(3, Int.MAX_VALUE))

    val fullBedrockSession = MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN
        .getFromInput(httpClient, msaDeviceCodeCallback)

    if (cache && file != null && !file.isDirectory) {
        val json = AuthUtils.gson.toJson(
            MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN.toJson(fullBedrockSession)
        )
        file.writeText(json)
    }

    return fullBedrockSession
}

fun StepFullBedrockSession.FullBedrockSession.refresh(): StepFullBedrockSession.FullBedrockSession {
    val httpClient = MinecraftAuth.createHttpClient()
    httpClient.connectTimeout = 1000
    httpClient.readTimeout = 2000
    httpClient.setRetryHandler(RetryHandler(0, Int.MAX_VALUE))
    return MinecraftAuth.BEDROCK_DEVICE_CODE_LOGIN.refresh(httpClient, this)
}
