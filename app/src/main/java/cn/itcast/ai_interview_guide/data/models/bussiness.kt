package cn.itcast.ai_interview_guide.data.models

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * 响应公共体
 */
@Serializable data class ResponseData<T> (
  /**
   * 请求成功10000标志
   */
  val code: Int,
  /**
   * 返回数据
   */
  val data: T? = null,
  /**
   * 请求成功
   */
  val message: String,
  /**
   * 请求成功标志
   */
  val success: Boolean
)

object FlexibleIntSerializer : KSerializer<Int> {
  override val descriptor: SerialDescriptor =
    PrimitiveSerialDescriptor("FlexibleInt", PrimitiveKind.INT)

  override fun serialize(encoder: Encoder, value: Int) = encoder.encodeInt(value)
  override fun deserialize(decoder: Decoder): Int {
    return try {
      decoder.decodeInt()
    } catch (e: Exception) {
      try {
        decoder.decodeString().toInt()
      } catch (e2: Exception) {
        0
      }
    }
  }
}